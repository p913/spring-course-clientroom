package ru.ptvi.otuscourse.clientroom.service;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.ptvi.otuscourse.clientroom.domain.*;
import ru.ptvi.otuscourse.clientroomdto.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    public static final int DEFAULT_DATE_INTERVAL_DAYS = 100;

    private final RoomApiService roomApiService;

    private final RoomApiAuthSevice roomApiAuthSevice;

    private final ModelMapper modelMapper;

    public RoomServiceImpl(RoomApiService roomApiService, RoomApiAuthSevice roomApiAuthSevice, ModelMapper modelMapper) {
        this.roomApiService = roomApiService;
        this.roomApiAuthSevice = roomApiAuthSevice;
        this.modelMapper = modelMapper;
    }

    private LocalDate parseDateToOrDefault(String dateTo) {
        return (dateTo == null || dateTo.isEmpty())
                ? LocalDate.now()
                : LocalDate.parse(dateTo);
    }

    private LocalDate parseDateFromOrDefault(String dateFrom, LocalDate dateTo) {
        return (dateFrom == null || dateFrom.isEmpty())
                ? dateTo.minusDays(DEFAULT_DATE_INTERVAL_DAYS)
                : LocalDate.parse(dateFrom);
    }

    @Override
    public Optional<ContragentWithDetailsDto> getContragentById(String id) {
        return roomApiService
                .getContragentById(roomApiAuthSevice.getAuthHeader(), id);
    }

    @Override
    public Optional<ContragentWithDetailsDto> getContragentByEmailOrPhone(String param) {
        var res = roomApiService
                .getContragentByEmailOrPhone(roomApiAuthSevice.getAuthHeader(), param, false);
        if (res.size() > 1)
            throw new IllegalStateException("Multiple accounts found for email or phone: " + param);

        return res.stream().findFirst();
    }

    @Override
    public List<DocuLinkWithContractDto> getDocsByDatesOrDefault(String contragentId, String dateFrom, String dateTo) {
        var dDateTo = parseDateToOrDefault(dateTo);
        var dDateFrom = parseDateFromOrDefault(dateFrom, dDateTo);

        return roomApiService
                .getContragentContracts(roomApiAuthSevice.getAuthHeader(), contragentId)
                .stream().parallel()
                .map(c ->
                    roomApiService.getContractDocuments(roomApiAuthSevice.getAuthHeader(), contragentId, c.id(), dDateFrom.toString(), dDateTo.toString())
                            .stream()
                            .map(d -> modelMapper.map(d, DocuLinkWithContractDto.class).contract(c)))
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> getNewNotifications(String contragentId) {
        return roomApiService.getContragentNotifications(roomApiAuthSevice.getAuthHeader(), contragentId, null, null);
    }

    @Override
    public List<NotificationDto> getNotificationsByDatesOrEmpty(String contragentId, String dateFrom, String dateTo) {
        var dDateTo = parseDateToOrDefault(dateTo);
        var dDateFrom = parseDateFromOrDefault(dateFrom, dDateTo);

        return roomApiService.getContragentNotifications(roomApiAuthSevice.getAuthHeader(), contragentId, dDateFrom.toString(), dDateTo.toString());
    }

    @Override
    public void markNotificationAsViewed(String contragentId, String[] notifications) {
        Arrays.stream(notifications).forEach(id ->
            roomApiService
                    .getNotificationById(roomApiAuthSevice.getAuthHeader(), contragentId, id)
                    .ifPresent(n -> {
                        n.viewed(true);
                        roomApiService.updateNotification(roomApiAuthSevice.getAuthHeader(), contragentId, id, n);
                    }));
    }

    @Override
    public List<DemandDto> getDemandsByDatesOrDefault(String contragentId, String dateFrom, String dateTo) {
        var dDateTo = parseDateToOrDefault(dateTo);
        var dDateFrom = parseDateFromOrDefault(dateFrom, dDateTo);

        return roomApiService.getContragentDemands(roomApiAuthSevice.getAuthHeader(), contragentId, dDateFrom.toString(), dDateTo.toString());
    }

    @Override
    public String pushDemand(String contragentId, DemandDto demand) {
        demand.decisionDateTime(null)
                .decisionNote(null)
                .demandDateTime(OffsetDateTime.now())
                .decisionSuccess(false);

        return roomApiService.createDemand(roomApiAuthSevice.getAuthHeader(), contragentId, demand);
    }

    @Override
    public List<PayWithContractDto> getPaysByDatesOrDefault(String contragentId, String dateFrom, String dateTo) {

        var dDateTo = parseDateToOrDefault(dateTo);
        var dDateFrom = parseDateFromOrDefault(dateFrom, dDateTo);

        return roomApiService
                .getContragentContracts(roomApiAuthSevice.getAuthHeader(), contragentId)
                .stream().parallel()
                .map(c ->
                        roomApiService.getContractPays(roomApiAuthSevice.getAuthHeader(), contragentId, c.id(), dDateFrom.toString(), dDateTo.toString())
                                .stream()
                                .map(p -> modelMapper.map(p, PayWithContractDto.class).contract(c)))
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    @Override
    public ChargesBothDto getChargesByDatesOrDefault(String contragentId, String dateFrom, String dateTo) {
        var dDateTo = parseDateToOrDefault(dateTo);
        var dDateFrom = parseDateFromOrDefault(dateFrom, dDateTo);

        return roomApiService
                .getContragentById(roomApiAuthSevice.getAuthHeader(), contragentId)
                .map(c -> new ChargesBothDto()
                        .chargeOnce(c.contracts().stream().parallel()
                                .map(ct ->
                                        roomApiService.getContractCharges(roomApiAuthSevice.getAuthHeader(), contragentId, ct.id(), dDateFrom.toString(), dDateTo.toString())
                                                .stream()
                                                .map(ch -> modelMapper.map(ch, ChargeOnceWithContractDto.class).contract(ct)))
                                .flatMap(Function.identity())
                                .collect(Collectors.toList()))
                        .chargeService(c.contracts().stream().parallel()
                                .map(ct -> ct.accObjects().stream().parallel()
                                        .map(ac ->
                                                roomApiService.getAccObjectCharges(roomApiAuthSevice.getAuthHeader(), contragentId, ct.id(), ac.id(), dDateFrom.toString(), dDateTo.toString())
                                                        .stream()
                                                        .map(ch -> modelMapper.map(ch, ChargeServiceWithAccObjectDto.class).accObject(ac)))
                                        .flatMap(Function.identity()))
                                .flatMap(Function.identity())
                                .collect(Collectors.toList()))
                        )
                .orElse(new ChargesBothDto());
    }
}
