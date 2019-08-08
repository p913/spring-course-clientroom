package ru.ptvi.otuscourse.clientroomexamples.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import ru.ptvi.otuscourse.clientroomdto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UseApiServiceImpl implements UseApiService {
    private static final String DIGITAL_TV_SERVICE_NAME = "Цифровой пакет ТВ-каналов";

    private static final String EQUIPMENT_SERVICE_NAME = "Аренда ТВ-приставки";

    private static final String DIGITAL_TV_ACC_OBJECT_NAME = "Подписка";

    private static final String EQUIPMENT_ACC_OBJECT_NAME = "ТВ-приставка";

    private ServiceDto digitalTvService;

    private ServiceDto equipmentService;

    private final String authHeader;

    private final RoomApiService roomApiService;

    public UseApiServiceImpl(@Value("${examples.security.api.username}") String apiUserName,
                             @Value("${examples.security.api.password}") String apiPassword,
                             RoomApiService roomApiService) {
        this.authHeader = "Basic " + Base64Utils.encodeToString((apiUserName + ":" + apiPassword).getBytes());
        this.roomApiService = roomApiService;
    }

    private void initServices() {
        if (digitalTvService == null || equipmentService == null) {

            var services = roomApiService.getAllServices(authHeader);

            var digitalTvService = services.stream().filter(s -> DIGITAL_TV_SERVICE_NAME.equals(s.name())).findAny();
            if (digitalTvService.isPresent())
                this.digitalTvService = digitalTvService.get();

            var equipmentService = services.stream().filter(s -> EQUIPMENT_SERVICE_NAME.equals(s.name())).findAny();
            if (equipmentService.isPresent())
                this.equipmentService = equipmentService.get();
        }

        if (digitalTvService == null)
            throw new RuntimeException("Service not found: " + DIGITAL_TV_SERVICE_NAME);

        if (equipmentService == null)
            throw new RuntimeException("Service not found: " + EQUIPMENT_SERVICE_NAME);
    }

    @Override
    public String createContragentAsPeople(String email, String phone, String firstName, String middleName,
                                           String lastName, String contract, String address) {

        initServices();

        var contragentDto = new ContragentDto()
                .peopleFirstName(firstName)
                .peopleLastName(lastName)
                .email(email)
                .phone(phone)
                .password("-");
        var contragentId = roomApiService.createContragent(authHeader, contragentDto);

        var contractDto = new ContractDto()
                .dateFrom(LocalDate.now())
                .balance(BigDecimal.valueOf(0L))
                .number(contract);

        var contractId = roomApiService.createContract(authHeader, contragentId, contractDto);

        var accObjectTvDto = new AccountingObjectDto()
                .name(DIGITAL_TV_ACC_OBJECT_NAME)
                .service(digitalTvService)
                .dateFrom(LocalDate.now());

        roomApiService.createAccObject(authHeader, contragentId, contractId, accObjectTvDto);

        var accObjectEqpmDto = new AccountingObjectDto()
                .name(EQUIPMENT_ACC_OBJECT_NAME)
                .service(equipmentService)
                .dateFrom(LocalDate.now());

        roomApiService.createAccObject(authHeader, contragentId, contractId, accObjectEqpmDto);

        return contragentId;
    }

    @Override
    public Optional<ContragentWithDetailsDto> getContragentById(String id) {
        var res =  roomApiService.getContragentById(authHeader, id);
        res.ifPresent(r -> r.password(""));
        return res;
    }

    @Override
    public List<ContragentWithDetailsDto> getContragentsByEmailOrPhone(String account) {
        var res =  roomApiService.getContragentsByEmailOrPhone(authHeader, account, true);
        res.forEach(r -> r.password(""));
        return res;
    }

    @Override
    public void chargeAndNotify(String id, int year, int month) {
        var dateFrom = LocalDate.of(year, month, 1);
        var dateTo = dateFrom.plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS);

        var contragent = roomApiService.getContragentById(authHeader, id);

        var notify = false;
        var summ = 0.0;

        for (var contract: contragent.orElseThrow(() -> new RuntimeException("Contragent not found: " + id)).contracts()) {
            double[] balance = { contract.balance().doubleValue() };
            for (var accObject: contract.accObjects()) {
                notify = true;

                //Снести старые услуги
                roomApiService.getServiceCharges(authHeader, id, contract.id(), accObject.id(), dateFrom.toString(), dateTo.toString())
                        .forEach(ch -> {
                            balance[0] += ch.summ().doubleValue();
                            roomApiService.deleteServiceCharges(authHeader, id, contract.id(), accObject.id(), ch.id());
                        });

                //Начислить заново
                var chargeDateFrom = dateFrom.compareTo(contract.dateFrom()) < 0 ? contract.dateFrom() : dateFrom;
                var chargeDateTo = contract.dateTo() != null && dateTo.compareTo(contract.dateTo()) > 0 ? contract.dateTo() : dateTo;
                var quantity = (double)(chargeDateFrom.until(chargeDateTo, ChronoUnit.DAYS) + 1) / (dateFrom.until(dateTo, ChronoUnit.DAYS) + 1);
                if (quantity > 0) {
                    var summ1 = quantity * accObject.service().cost().doubleValue();
                    var chargeDto = new ChargeServiceDto()
                            .dateFrom(chargeDateFrom)
                            .dateTo(chargeDateTo)
                            .service(accObject.service())
                            .quantity(quantity)
                            .cost(accObject.service().cost())
                            .summ(BigDecimal.valueOf((long)(summ1 * 100), 2));
                    balance[0] -= summ1;
                    summ += summ1;
                    roomApiService.createServiceCharges(authHeader, id, contract.id(), accObject.id(), chargeDto);
                }
            }
            //Обновить баланс по договору
            contract.balance(BigDecimal.valueOf((long)(balance[0] * 100), 2));
            roomApiService.updateContract(authHeader, id, contract.id(), contract);
        }

        if (notify) {
            var notificationDto = new NotificationDto()
                    .message(String.format("Произведено списание по ежемесячным услугам на сумму %3.2fр", summ))
                    .originDateTime(OffsetDateTime.now())
                    .sendEmail(true)
                    .sendSms(true);
            roomApiService.createNotification(authHeader, id, notificationDto);
        }

    }
}

