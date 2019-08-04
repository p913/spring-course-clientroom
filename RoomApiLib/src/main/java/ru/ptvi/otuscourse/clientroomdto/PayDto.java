package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "Pay", description = "Pay for contract")
public class PayDto {
    private String id;

    @ApiModelProperty(position = 1)
    private OffsetDateTime payDateTime;

    @ApiModelProperty(position = 2)
    private BigDecimal summ;

    @ApiModelProperty(position = 3, notes = "Payment system, bank, etc...")
    private String source;

    @ApiModelProperty(position = 4)
    private String transactionNumber;

    @ApiModelProperty(position = 5)
    private String documentNumber;

}
