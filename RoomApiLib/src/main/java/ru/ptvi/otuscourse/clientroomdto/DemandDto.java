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
@ApiModel(value = "Demand", description = "Demand from contragent. May be accepted or rejected.")
public class DemandDto {
    private String id;

    @ApiModelProperty(position = 1, notes = "Present only if demand related to contract")
    private ContractDto contract;

    @ApiModelProperty(position = 2, notes = "Present only if demand related to accounting object")
    private AccountingObjectDto accObject;

    @ApiModelProperty(position = 3, notes = "PAUSE, RESUME, STOP, SERVICE or OTHER. Usually for automated processing.")
    private String demandSubject;

    @ApiModelProperty(position = 4, notes = "Human-readable or automated processed text")
    private String demandNote;

    @ApiModelProperty(position = 5)
    private OffsetDateTime demandDateTime;

    @ApiModelProperty(position = 6, notes = "True if the demand is successfully completed or false if the demand is rejected or has not yet been reviewed.")
    private boolean decisionSuccess;

    @ApiModelProperty(position = 7)
    private String decisionNote;

    @ApiModelProperty(position = 8)
    private OffsetDateTime decisionDateTime;

}
