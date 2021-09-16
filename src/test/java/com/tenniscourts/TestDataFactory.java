package com.tenniscourts;

import com.tenniscourts.guests.CreateGuestRequestDTO;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.reservations.ReservationDTO;
import com.tenniscourts.reservations.ReservationStatus;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class TestDataFactory {

    public static GuestDTO createGuestDTO(String name, Long id) {
        return GuestDTO.builder().name(name).id(id).build();
    }

    public static CreateGuestRequestDTO createGuestRequestDTO(String name) {
        return CreateGuestRequestDTO.builder().name(name).build();
    }

    public static Guest createGuest(String name) {
        return Guest.builder().name(name).build();
    }

    public static TennisCourtDTO createTennisCourtDTO(String name, Long id) {
        return TennisCourtDTO.builder().name(name).id(id).build();
    }

    public static TennisCourt createTennisCourt(String name) {
        return TennisCourt.builder().name(name).build();
    }

    public static Schedule createSchedule(LocalDateTime startDateTime, String tennisCourtName) {
        return Schedule.builder().startDateTime(startDateTime).endDateTime(startDateTime.plusHours(1L))
                       .tennisCourt(createTennisCourt(tennisCourtName)).reservations(new ArrayList<>()).build();
    }

    public static ScheduleDTO createScheduleDTO(Long scheduleId, LocalDateTime startDateTime,
                                                   TennisCourtDTO tennisCourtDTO) {
        return ScheduleDTO.builder().startDateTime(startDateTime).endDateTime(startDateTime.plusHours(1L))
                          .tennisCourt(tennisCourtDTO).tennisCourtId(
                        tennisCourtDTO.getId()).id(scheduleId).build();
    }

    public static Reservation createReservation(GuestDTO guestDTO, ScheduleDTO scheduleDTO,
                                                   TennisCourtDTO tennisCourtDTO) {
        return Reservation.builder().reservationStatus(ReservationStatus.READY_TO_PLAY).guest(createGuest(
                                  guestDTO.getName())).refundValue(BigDecimal.TEN).value(BigDecimal.TEN)
                          .schedule(createSchedule(scheduleDTO.getStartDateTime(), tennisCourtDTO.getName())).build();
    }

    public static ReservationDTO createReservationDTO(Long reservationId, GuestDTO guestDTO,
                                                         ScheduleDTO scheduleDTO) {
        return ReservationDTO.builder().reservationStatus(ReservationStatus.READY_TO_PLAY.name())
                             .refundValue(BigDecimal.TEN).value(BigDecimal.TEN).guestId(
                        guestDTO.getId()).schedule(scheduleDTO).scheduledId(scheduleDTO.getId())
                             .previousReservation(null).id(reservationId).build();
    }
}
