package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "Contragent", description = "May be people or firm. Authenticated by e-mail or phone")
public class ContragentDto {
    @ApiModelProperty
    private String id;

    @ApiModelProperty(position = 1)
    private String email;

    @ApiModelProperty(position = 2, notes = "People id if contragent is people")
    private String peopleId;

    @ApiModelProperty(position = 3, notes = "First name if contragent is people")
    private String peopleFirstName;

    @ApiModelProperty(position = 4, notes = "Middle name if contragent is people, may be null")
    private String peopleMiddleName;

    @ApiModelProperty(position = 5, notes = "Last name if contragent is people")
    private String peopleLastName;

    @ApiModelProperty(position = 6, notes = "Passport series and number if contragent is people")
    private String peoplePassport;

    @ApiModelProperty(position = 7, notes = "Birthday if contragent is people")
    private LocalDate peopleBirthday;

    @ApiModelProperty(position = 8, notes = "Firm id if contragent is firm")
    private String firmId;

    @ApiModelProperty(position = 9, notes = "Firm name if contragent is firm")
    private String firmName;

    @ApiModelProperty(position = 10, notes = "Firm INN if contragent is firm")
    private String firmInn;

    @ApiModelProperty(position = 11, notes = "Firm KPP if contragent is firm")
    private String firmKpp;

    @ApiModelProperty(position = 12, notes = "Firm Bank name, BIC and corr. account if contragent is firm")
    private String firmBank;

    @ApiModelProperty(position = 13, notes = "Firm bank account if contragent is firm")
    private String firmAccount;

    @ApiModelProperty(position = 14, notes = "Post address with ZIP, city, street, flat/office, etc...")
    private String address;

    @ApiModelProperty(position = 15, notes = "Phone number")
    private String phone;

    @ApiModelProperty(position = 16, notes = "Encoded password")
    private String password;

}
