package ru.ptvi.otuscourse.clientroomapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contragent_id")
    @NotNull
    private Contragent contragent;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "acc_object_id")
    private AccountingObject accObject;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DemandSubject demandSubject;

    private String demandNote;

    @NotNull
    private OffsetDateTime demandDateTime;

    private boolean decisionSuccess;

    private String decisionNote;

    private OffsetDateTime decisionDateTime;

    public Demand(UUID id) {
        this.id = id;
    }

    public Demand(UUID id, Contragent contragent, Contract contract, AccountingObject accObject,
                   DemandSubject demandSubject, String demandNote, OffsetDateTime demandDateTime,
                   boolean decisionSuccess, String decisionNote, OffsetDateTime decisionDateTime) {
        this.id = id;
        this.contragent = contragent;
        this.contract = contract;
        this.accObject = accObject;
        this.demandSubject = demandSubject;
        this.demandNote = demandNote;
        this.demandDateTime = demandDateTime;
        this.decisionSuccess = decisionSuccess;
        this.decisionNote = decisionNote;
        this.decisionDateTime = decisionDateTime;
    }

    public Demand(Contragent contragent, Contract contract, AccountingObject accObject,
                  DemandSubject demandSubject, String demandNote, OffsetDateTime demandDateTime,
                  boolean decisionSuccess, String decisionNote, OffsetDateTime decisionDateTime) {
        this(null, contragent, contract, accObject, demandSubject, demandNote, demandDateTime,
                decisionSuccess, decisionNote, decisionDateTime);
    }

    public Demand(Contragent contragent, Contract contract, AccountingObject accObject,
                  DemandSubject demandSubject, String demandNote, OffsetDateTime demandDateTime) {
        this(null, contragent, contract, accObject, demandSubject, demandNote, demandDateTime,
                false, null, null);
    }

}
