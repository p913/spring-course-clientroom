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
@ApiModel(value = "ChargeOnce", description = "One-time charge for contract")
public class ChargeOnceDto {
    private String id;

    @ApiModelProperty(position = 1)
    private LocalDate dateFrom;

    @ApiModelProperty(position = 2)
    private LocalDate dateTo;

    @ApiModelProperty(position = 3)
    private String description;

    @ApiModelProperty(position = 4)
    private String metric;

    @ApiModelProperty(position = 5)
    private BigDecimal cost;

    @ApiModelProperty(position = 6)
    private double quantity;

    @ApiModelProperty(position = 7)
    private BigDecimal summ;
}
