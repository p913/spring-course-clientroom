package ru.ptvi.otuscourse.clientroomapi.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Firm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String name;

    @Size(max = 12)
    private String inn;

    @Size(max = 9)
    private String kpp;

    private String bank;

    private String account;

    public Firm(UUID id) {
        this.id = id;
    }

    public Firm(UUID id, String name, String inn, String kpp, String bank, String account) {
        this.id = id;
        this.name = name;
        this.inn = inn;
        this.kpp = kpp;
        this.bank = bank;
        this.account = account;
    }

    public Firm(String name, String inn, String kpp, String bank, String account) {
        this(null, name, inn, kpp, bank, account);
    }

}
