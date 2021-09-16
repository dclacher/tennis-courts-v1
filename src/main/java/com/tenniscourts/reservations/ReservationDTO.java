package com.tenniscourts.reservations;

import com.tenniscourts.schedules.ScheduleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@ApiModel(description = "Representation of a reservation")
public class ReservationDTO {

    @ApiModelProperty(notes = "The ID of the reservation")
    private Long id;

    @ApiModelProperty(notes = "The schedule for the reservation")
    private ScheduleDTO schedule;

    @ApiModelProperty(notes = "The reservation status")
    private String reservationStatus;

    @ApiModelProperty(notes = "The previous reservation")
    private ReservationDTO previousReservation;

    @ApiModelProperty(notes = "The refund value for the reservation")
    private BigDecimal refundValue;

    @ApiModelProperty(notes = "The deposit value for the reservation")
    private BigDecimal value;

    @NotNull
    @ApiModelProperty(notes = "The ID of the schedule for the reservation")
    private Long scheduledId;

    @NotNull
    @ApiModelProperty(notes = "The ID of the guest for the reservation")
    private Long guestId;
}
