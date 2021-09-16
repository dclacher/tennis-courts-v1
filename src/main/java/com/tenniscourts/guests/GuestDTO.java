package com.tenniscourts.guests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Representation of a guest")
public class GuestDTO {

    @NotNull
    @ApiModelProperty(notes = "The ID of the guest", example = "1")
    private Long id;

    @NotNull
    @ApiModelProperty(notes = "The name of the guest", example = "Daniil Medvedev")
    private String name;

}
