package com.tenniscourts.reservations;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@ApiModel(description = "Representation of a new reservation request")
public class CreateReservationRequestDTO {

    @NotNull
    @ApiModelProperty(notes = "The ID of the guest requesting the reservation")
    private Long guestId;

    @NotNull
    @ApiModelProperty(notes = "The ID of the schedule for the reservation")
    private Long scheduleId;

}
