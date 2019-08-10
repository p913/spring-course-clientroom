package ru.ptvi.otuscourse.clientroom.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RegDataDto {
    @NotNull
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    private String contract;

    @NotNull
    private String date;

}
