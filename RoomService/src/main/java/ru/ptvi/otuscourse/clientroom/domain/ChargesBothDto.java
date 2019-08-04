package ru.ptvi.otuscourse.clientroom.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChargesBothDto {
   private List<ChargeOnceWithContractDto> chargeOnce = new ArrayList<>();

   private List<ChargeServiceWithAccObjectDto> chargeService = new ArrayList<>();
}
