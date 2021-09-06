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