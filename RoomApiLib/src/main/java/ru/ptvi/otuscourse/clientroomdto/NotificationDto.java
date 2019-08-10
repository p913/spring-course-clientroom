package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "Notification", description = "Notification for contragent. May be sended via e-mail or sms.")
public class NotificationDto {
    private String id;

    @ApiModelProperty(position = 1)
    private OffsetDateTime originDateTime;

    @ApiModelProperty(position = 2)
    private String message;

    @ApiModelProperty(position = 3)
    private boolean sendEmail;

    @ApiModelProperty(position = 4)
    private boolean sendSms;

    @ApiModelProperty(position = 5, notes = "Contragent saw the notification on site or mobile application")
    private boolean viewed;
}
