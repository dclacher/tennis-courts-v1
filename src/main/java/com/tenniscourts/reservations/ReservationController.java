package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
@Api(tags = "Reservations Management")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation(value = "bookReservation", tags = "Book reservations", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request")})
    public ResponseEntity<Void> bookReservation(
            @ApiParam(value = "Create reservation request", required = true) @RequestBody
                    CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(
                locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    public ResponseEntity<ReservationDTO> findReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    public ResponseEntity<ReservationDTO> cancelReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "rescheduleReservation", tags = "Reschedule a reservation", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request"),
            @ApiResponse(code = 404, message = "Reservation or schedule not found")})
    public ResponseEntity<ReservationDTO> rescheduleReservation(
            @ApiParam(value = "Reservation ID", required = true) @PathVariable(name = "id") Long reservationId,
            @ApiParam(value = "Schedule ID", required = true) @RequestParam("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
