package com.tenniscourts.guests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Representation of a guest")
public class GuestDTO {

    @ApiModelProperty(notes = "The ID of the guest")
    private Long id;

    @NotNull
    @ApiModelProperty(notes = "The name of the guest")
    private String name;

}
