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
public class ChargeOnce {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    @NotNull
    private Contract contract;

    @NotNull
    private LocalDate dateFrom;

    @NotNull
    private LocalDate dateTo;

    @NotNull
    private String description;

    private String metric;

    @NotNull
    private BigDecimal cost;

    private double quantity;

    @NotNull
    private BigDecimal summ;

    public ChargeOnce(UUID id) {
        this.id = id;
    }

    public ChargeOnce(UUID id, Contract contract, LocalDate dateFrom, LocalDate dateTo, String description, String metric, BigDecimal cost, double quantity, BigDecimal summ) {
        this.id = id;
        this.contract = contract;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.description = description;
        this.metric = metric;
        this.cost = cost;
        this.quantity = quantity;
        this.summ = summ;
    }

    public ChargeOnce(Contract contract, LocalDate dateFrom, LocalDate dateTo, String description, String metric, BigDecimal cost, double quantity, BigDecimal summ) {
        this(null, contract, dateFrom, dateTo, description, metric, cost, quantity, summ);
    }
}