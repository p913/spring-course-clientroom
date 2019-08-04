package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "ChargeService", description = "Reqular charge for accounting object")
public class ChargeServiceDto {
    private String id;

    @ApiModelProperty(position = 1)
    private ServiceDto service;

    @ApiModelProperty(position = 2)
    private LocalDate dateFrom;

    @ApiModelProperty(position = 3)
    private LocalDate dateTo;

    @ApiModelProperty(position = 4)
    private String metric;

    @ApiModelProperty(position = 5)
    private BigDecimal cost;

    @ApiModelProperty(position = 6)
    private double quantity;

    @ApiModelProperty(position = 7)
    private BigDecimal summ;
}
