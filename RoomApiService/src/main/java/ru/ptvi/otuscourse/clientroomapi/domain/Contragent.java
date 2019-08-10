package ru.ptvi.otuscourse.clientroomapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Contragent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "people_id")
    private People people;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "firm_id")
    private Firm firm;

    private String address;

    private String phone;

    private String password;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contragent")
    private List<Contract> contracts = new ArrayList<>();

    public Contragent(UUID id) {
        this.id = id;
    }

    public Contragent(UUID id, String email, People people, Firm firm, String address, String phone, String password) {
        this.id = id;
        this.email = email;
        this.people = people;
        this.firm = firm;
        this.address = address;
        this.phone = phone;
        this.password = password;
    }

    public Contragent(String email, People people, Firm firm, String address, String phone, String password) {
        this(null, email, people, firm, address, phone, password);
    }

}
