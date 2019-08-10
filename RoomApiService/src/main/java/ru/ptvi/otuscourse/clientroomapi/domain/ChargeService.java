package ru.ptvi.otuscourse.clientroomapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChargeService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "acc_object_id")
    @NotNull
    private AccountingObject accObject;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @NotNull
    private Service service;

    @NotNull
    private LocalDate dateFrom;

    @NotNull
    private LocalDate dateTo;

    private String metric;

    @NotNull
    private BigDecimal cost;

    private double quantity;

    @NotNull
    private BigDecimal summ;

    public ChargeService(UUID id) {
        this.id = id;
    }


    public ChargeService(UUID id, AccountingObject accObject, Service service, LocalDate dateFrom, LocalDate dateTo,
                         String metric, BigDecimal cost, double quantity, BigDecimal summ) {
        this.id = id;
        this.accObject = accObject;
        this.service = service;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.metric = metric;
        this.cost = cost;
        this.quantity = quantity;
        this.summ = summ;
    }

    public ChargeService(AccountingObject accObject, Service service, LocalDate dateFrom, LocalDate dateTo, String metric, BigDecimal cost, double quantity, BigDecimal summ) {
        this(null, accObject, service, dateFrom, dateTo, metric, cost, quantity, summ);
    }

}
