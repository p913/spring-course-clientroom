package ru.ptvi.otuscourse.clientroomapi.domain;

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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contragent_id")
    @NotNull
    private Contragent contragent;

    @NotNull
    private OffsetDateTime originDateTime;

    @NotNull
    private String message;

    private boolean sendEmail;

    private boolean sendSms;

    private boolean viewed;

    public Notification(UUID id) {
        this.id = id;
    }

    public Notification(UUID id, Contragent contragent, OffsetDateTime originDateTime, String message, boolean sendEmail, boolean sendSms, boolean viewed) {
        this.id = id;
        this.contragent = contragent;
        this.originDateTime = originDateTime;
        this.message = message;
        this.sendEmail = sendEmail;
        this.sendSms = sendSms;
        this.viewed = viewed;
    }

    public Notification(Contragent contragent, OffsetDateTime originDateTime, String message, boolean sendEmail, boolean sendSms, boolean viewed) {
        this (null, contragent, originDateTime, message, sendEmail, sendSms, viewed);
    }
}
