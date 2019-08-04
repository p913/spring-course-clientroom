package ru.ptvi.otuscourse.clientroomapi.repository;

import ru.ptvi.otuscourse.clientroomapi.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityForTest {
    private static Random random = new Random();
    private static AtomicInteger numberContract = new AtomicInteger();
    private static AtomicInteger numberService = new AtomicInteger();
    private static AtomicInteger numberAccObject = new AtomicInteger();

    private static LocalDate createRandomDate() {
        return LocalDate.of(2000 + random.nextInt(19),
                1 + random.nextInt(12),
                1 + random.nextInt(28));
    }

    private static OffsetDateTime currentDateTime() {
        return OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static People createPeople() {
        return new People(
                "First Name " + random.nextInt(128),
                null,
                "Last Name" + random.nextInt(128),
                String.format("%04d %06d", random.nextInt(9999), random.nextInt(999999)),
                createRandomDate());
    }

    public static Firm createFirm() {
        return new Firm(
                "Roga & kopyta " + random.nextInt(128),
                String.format("615%07d", random.nextInt(10000)),
                String.format("615%06d", random.nextInt(10000)),
                "PAO Sberbank BIC 41693837",
                String.format("40891234569%09d", random.nextInt(1000000000)));
    }

    public static Contragent createContragentAsPeople(String email) {
        return new Contragent(
                email,
                createPeople(),
                null,
                "Moscow, Kremlin",
                "+79091234567",
                "password");
    }

    public static Contragent createContragentAsFirm(String email) {
        return new Contragent(
                email,
                null,
                createFirm(),
                "Moscow, Kremlin",
                "+79091234567",
                "password");
    }

    public static Contract createContract(Contragent contragent, double balance) {
        LocalDate dateFrom = createRandomDate();
        return new Contract(
                contragent,
                String.format("%04d/%04d", numberContract.incrementAndGet(), numberContract.incrementAndGet()),
                dateFrom,
                dateFrom.plus(1, ChronoUnit.DAYS),
                BigDecimal.valueOf((int)(balance * 100), 2));
    }

    public static Service createService() {
        return new Service(
                "Basic service " + numberService.incrementAndGet(),
                BigDecimal.valueOf(random.nextInt(1000), 2));
    }

    public static AccountingObject createAccountingObject(Contract contract, Service service) {
        LocalDate dateFrom = createRandomDate();
        return new AccountingObject(
                contract,
                service,
                "Accounting object " + numberAccObject.incrementAndGet(),
                "desription of accounting object - long text",
                dateFrom,
                dateFrom.plus(1, ChronoUnit.DAYS),
                1);
    }

    public static DocuLink createDocument(Contract contract) {
        return new DocuLink(
                contract,
                createRandomDate(),
                "Title " + random.nextInt(100),
                "docs.ptvi.ru/?" + UUID.randomUUID());
    }

    public static Pay createPay(Contract contract) {
        return new Pay(
                contract,
                currentDateTime(),
                BigDecimal.valueOf(random.nextInt(10000), 2),
                "Online",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
    }

    public static Notification createNotification(Contragent contragent) {
        return new Notification(
                contragent,
                currentDateTime(),
                "You have new bill",
                true,
                false,
                false);
    }

    public static ChargeOnce createChargeOnce(Contract contract) {
        LocalDate dateFrom = createRandomDate();
        int price = random.nextInt(10000);
        return new ChargeOnce(
            contract,
            dateFrom,
            dateFrom.plus(1, ChronoUnit.DAYS),
            "Work " + random.nextInt(1000),
            "pcs",
            BigDecimal.valueOf(price, 2),
            10,
            BigDecimal.valueOf(10 * price, 2)
        );
    }

    public static ChargeService createChargeService (AccountingObject accObject, Service service) {
        LocalDate dateFrom = createRandomDate();
        int price = random.nextInt(10000);
        return new ChargeService(
                accObject,
                service,
                dateFrom,
                dateFrom.plus(1, ChronoUnit.DAYS),
                "pcs",
                BigDecimal.valueOf(price, 2),
                10,
                BigDecimal.valueOf(10 * price, 2)
        );
    }

    public static Demand createDemand(Contragent contragent, Contract contract, AccountingObject accObject) {
        return new Demand(
                contragent,
                contract,
                accObject,
                DemandSubject.PAUSE,
                "Note for demand",
                currentDateTime(),
                true,
                "Note for decision",
                currentDateTime()
        );
    }
}
