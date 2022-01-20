package com.tenniscourts.reservations;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ApiModel(description = "Representation of a new reservation request")
public class CreateReservationRequestDTO {

    @NotNull
    @ApiModelProperty(notes = "The ID of the guest requesting the reservation", example = "1")
    private Long guestId;

    @NotNull
    @ApiModelProperty(notes = "The ID of the schedule for the reservation", example = "1")
    private Long scheduleId;

}
