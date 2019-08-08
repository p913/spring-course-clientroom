package ru.ptvi.otuscourse.clientroomapi.notify;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
public class NotifyMessagePayload {
    private String email;

    private String phone;

    private String message;

    private boolean sendSms;

    private boolean sendEmail;
}
