package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "ContragentWithContracts", description = "May be people or firm. Authenticated by e-mail or phone")
public class ContragentWithDetailsDto extends ContragentDto {
    @ApiModelProperty(position = 16, notes = "Contracts list, if requested")
    private List<ContractWithDetailsDto> contracts = new ArrayList<>();

}
