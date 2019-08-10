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
@ApiModel(value = "ContractWithAccountingObjects", description = "Contragent contract, may contain accounting object and have charges and pays")
public class ContractWithDetailsDto extends ContractDto {
    @ApiModelProperty(position = 6, notes = "Accounting objects list, if requested")
    private List<AccountingObjectDto> accObjects = new ArrayList<>();
}
