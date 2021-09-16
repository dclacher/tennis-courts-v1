package com.tenniscourts.guests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ApiModel(description = "Representation of a new guest request")
public class CreateGuestRequestDTO {

    @NotNull
    @ApiModelProperty(notes = "The name of the new guest", example = "Novak Djokovic")
    private String name;
}
