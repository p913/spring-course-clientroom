package ru.ptvi.otuscourse.clientroomapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    @NotNull
    private Contract contract;

    @NotNull
    private OffsetDateTime payDateTime;

    @NotNull
    private BigDecimal summ;

    @NotNull
    private String source;

    @NotNull
    private String transactionNumber;

    @NotNull
    private String documentNumber;

    public Pay(UUID id) {
        this.id = id;
    }

    public Pay(UUID id, Contract contract, OffsetDateTime payDateTime, BigDecimal summ, String source, String transactionNumber, String documentNumber) {
        this.id = id;
        this.contract = contract;
        this.payDateTime = payDateTime;
        this.summ = summ;
        this.source = source;
        this.transactionNumber = transactionNumber;
        this.documentNumber = documentNumber;
    }

    public Pay(Contract contract, OffsetDateTime payDateTime, BigDecimal summ, String source, String transactionNumber, String documentNumber) {
        this (null, contract, payDateTime, summ, source, transactionNumber, documentNumber);
    }
}
