package ru.ptvi.otuscourse.clientroomapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccountingObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    @NotNull
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @NotNull
    private Service service;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private LocalDate dateFrom;

    private LocalDate dateTo;

    @NotNull
    private double quantity;

    public AccountingObject(UUID id) {
        this.id = id;
    }

    public AccountingObject(UUID id, Contract contract, Service service, String name, String description, LocalDate dateFrom, LocalDate dateTo, double quantity) {
        this.id = id;
        this.contract = contract;
        this.service = service;
        this.name = name;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.quantity = quantity;
    }

    public AccountingObject(Contract contract, Service service, String name, String description, LocalDate dateFrom, LocalDate dateTo, double quantity) {
        this (null, contract, service, name, description, dateFrom, dateTo, quantity);
    }
}
