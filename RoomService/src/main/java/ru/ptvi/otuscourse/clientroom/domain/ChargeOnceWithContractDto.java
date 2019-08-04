package ru.ptvi.otuscourse.clientroom.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.ptvi.otuscourse.clientroomdto.ChargeOnceDto;
import ru.ptvi.otuscourse.clientroomdto.ContractDto;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChargeOnceWithContractDto extends ChargeOnceDto {
    private ContractDto contract;
}
