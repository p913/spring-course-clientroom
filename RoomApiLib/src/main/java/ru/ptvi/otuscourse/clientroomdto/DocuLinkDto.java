package ru.ptvi.otuscourse.clientroomdto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ApiModel(value = "Document", description = "Document for contract. Link only, no content")
public class DocuLinkDto {
    private String id;

    @ApiModelProperty(position = 1)
    private LocalDate date;

    @ApiModelProperty(position = 2)
    private String title;

    @ApiModelProperty(position = 3)
    private String url;
}
