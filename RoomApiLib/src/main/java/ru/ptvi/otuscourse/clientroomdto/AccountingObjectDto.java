package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "AccountingObject", description = "Accounting object for contract")
public class AccountingObjectDto {
    private String id;

    @ApiModelProperty(position = 1, notes = "Reqular service for accounting object")
    private ServiceDto service;

    @ApiModelProperty(position = 2)
    private String name;

    @ApiModelProperty(position = 3)
    private String description;

    @ApiModelProperty(position = 4, notes = "Begin date, always present")
    private LocalDate dateFrom;

    @ApiModelProperty(position = 5, notes = "End date, accounting stopped if end date present")
    private LocalDate dateTo;
}
