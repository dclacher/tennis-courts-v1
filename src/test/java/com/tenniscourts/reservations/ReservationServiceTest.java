package com.tenniscourts.reservations;

import com.tenniscourts.TestDataFactory;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestDTO;
import com.tenniscourts.guests.GuestMapper;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.*;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationMapper reservationMapper;

    @Mock
    GuestService guestService;

    @Mock
    GuestMapper guestMapper;

    @Mock
    ScheduleService scheduleService;

    @Mock
    ScheduleMapper scheduleMapper;

    @Test
    public void rescheduleReservation() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("John Doe", 511L);
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Court 40", 402L);
        ScheduleDTO oldScheduleDTO =
                TestDataFactory.createScheduleDTO(61L, LocalDateTime.now().plusDays(1L), tennisCourtDTO);
        ScheduleDTO newScheduleDTO =
                TestDataFactory.createScheduleDTO(62L, LocalDateTime.now().plusDays(2L), tennisCourtDTO);
        Reservation previousReservation = TestDataFactory.createReservation(guestDTO, oldScheduleDTO, tennisCourtDTO);
        ReservationDTO newReservationDTO = TestDataFactory.createReservationDTO(141L, guestDTO, newScheduleDTO);
        previousReservation.setId(131L);

        Mockito.doReturn(Optional.of(previousReservation)).when(reservationRepository).findById(Mockito.anyLong());
        Mockito.doReturn(previousReservation).when(reservationRepository).save(Mockito.any(Reservation.class));
        Mockito.doReturn(newReservationDTO).when(reservationMapper).map(Mockito.any(Reservation.class));
        Mockito.doReturn(newScheduleDTO).when(scheduleService).findSchedule(Mockito.anyLong());

        ReservationDTO response = reservationService.rescheduleReservation(131L, newScheduleDTO.getId());

        Assert.assertEquals(ReservationStatus.READY_TO_PLAY.name(), response.getReservationStatus());
        Assert.assertNotNull(response.getPreviousReservation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rescheduleReservation_sameScheduleId() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("John Wayne", 51L);
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Court 40", 40L);
        ScheduleDTO scheduleDTO = TestDataFactory.createScheduleDTO(99L, LocalDateTime.now(), tennisCourtDTO);
        Reservation previousReservation = TestDataFactory.createReservation(guestDTO, scheduleDTO, tennisCourtDTO);
        previousReservation.setId(111L);

        Mockito.doReturn(Optional.of(previousReservation)).when(reservationRepository).findById(Mockito.anyLong());

        reservationService.rescheduleReservation(111L, scheduleDTO.getId());
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void rescheduleReservation_ReservationNotFound() {
        Mockito.doThrow(new EntityNotFoundException("Reservation not found")).when(reservationRepository)
               .findById(Mockito.anyLong());

        reservationService.rescheduleReservation(100L, 1L);
    }

    @Test
    public void bookReservation() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("John McEnroe", 10L);
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Court 456", 456L);
        ScheduleDTO scheduleDTO = TestDataFactory.createScheduleDTO(333L, LocalDateTime.now(), tennisCourtDTO);
        Reservation reservation = TestDataFactory.createReservation(guestDTO, scheduleDTO, tennisCourtDTO);

        Mockito.doReturn(guestDTO).when(guestService).findGuest(Mockito.anyLong());
        Mockito.doReturn(scheduleDTO).when(scheduleService).findSchedule(Mockito.anyLong());
        Mockito.doReturn(new Guest()).when(guestMapper).map(Mockito.any(GuestDTO.class));
        Mockito.doReturn(new Schedule()).when(scheduleMapper).map(Mockito.any(ScheduleDTO.class));
        Mockito.doReturn(reservation)
               .when(reservationRepository).save(Mockito.any(Reservation.class));
        Mockito.doReturn(TestDataFactory.createReservationDTO(765L, guestDTO, scheduleDTO)).when(reservationMapper)
               .map(Mockito.any(Reservation.class));
        Mockito.doReturn(scheduleDTO).when(scheduleService)
               .updateSchedule(Mockito.anyLong(), Mockito.any(Reservation.class));

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO();
        createReservationRequestDTO.setGuestId(guestDTO.getId());
        createReservationRequestDTO.setScheduleId(scheduleDTO.getId());
        ReservationDTO response = reservationService.bookReservation(createReservationRequestDTO);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getSchedule().getId(), createReservationRequestDTO.getScheduleId());
        Assert.assertEquals(response.getGuestId(), createReservationRequestDTO.getGuestId());
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void bookReservation_GuestNotFound() {
        Mockito.doThrow(new EntityNotFoundException("Guest not found")).when(guestService).findGuest(Mockito.anyLong());

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO();
        createReservationRequestDTO.setGuestId(123L);
        reservationService.bookReservation(createReservationRequestDTO);
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void bookReservation_ScheduleNotFound() {
        Mockito.doThrow(new EntityNotFoundException("Schedule not found")).when(scheduleService)
               .findSchedule(Mockito.anyLong());

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO();
        createReservationRequestDTO.setScheduleId(123L);
        reservationService.bookReservation(createReservationRequestDTO);
    }

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(
                Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }
}