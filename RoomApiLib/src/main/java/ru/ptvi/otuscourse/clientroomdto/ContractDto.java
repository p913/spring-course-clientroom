package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "Contract", description = "Contragent contract, may contain accounting object and have charges and pays")
public class ContractDto {
    private String id;

    @ApiModelProperty(position = 1)
    private String number;

    @ApiModelProperty(position = 3, notes = "Begin date, always present")
    private LocalDate dateFrom;

    @ApiModelProperty(position = 4, notes = "End date, contract closed if end date present")
    private LocalDate dateTo;

    @ApiModelProperty(position = 5, notes = "Debt is negative")
    private BigDecimal balance;

}

