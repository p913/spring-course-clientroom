package ru.ptvi.otuscourse.clientroomapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contragent_id")
    @NotNull
    private Contragent contragent;

    @NotNull
    private String number;

    @NotNull
    private LocalDate dateFrom;

    private LocalDate dateTo;

    @NotNull
    private BigDecimal balance;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @BatchSize(size = 10)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    private List<AccountingObject> accObjects = new ArrayList<>();

    public Contract(UUID id) {
        this.id = id;
    }

    public Contract(UUID id, Contragent contragent, String number, LocalDate dateFrom, LocalDate dateTo, BigDecimal balance) {
        this.id = id;
        this.contragent = contragent;
        this.number = number;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.balance = balance;
    }

    public Contract(Contragent contragent, String number, LocalDate dateFrom, LocalDate dateTo, BigDecimal balance) {
        this(null, contragent, number, dateFrom, dateTo, balance);
    }
}
