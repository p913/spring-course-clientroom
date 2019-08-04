package ru.ptvi.otuscourse.clientroomapi.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private String lastName;

    private String passport;

    private LocalDate birthday;

    public People(UUID id) {
        this.id = id;
    }

    public People(UUID id, String firstName, String middleName, String lastName, String passport, LocalDate birthday) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.passport = passport;
        this.birthday = birthday;
    }

    public People(String firstName, String middleName, String lastName, String passport, LocalDate birthday) {
        this(null, firstName, middleName, lastName, passport, birthday);
    }

}
