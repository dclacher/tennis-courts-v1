package com.tenniscourts.schedules;

import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import org.junit.Assert;
import org.junit.Before;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ScheduleService.class)
public class ScheduleServiceTest {

    @InjectMocks
    ScheduleService scheduleService;

    @Mock
    ScheduleMapper scheduleMapper;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    TennisCourtRepository tennisCourtRepository;

    Schedule schedule;
    ScheduleDTO scheduleDTO;
    TennisCourt tennisCourt;
    CreateScheduleRequestDTO createScheduleRequestDTO;

    @Before
    public void setUp() {
        // Preparing objects to be used in the addSchedule test
        tennisCourt = new TennisCourt();
        tennisCourt.setName("Arthur Ashe Stadium");
        tennisCourt.setId(123L);
        TennisCourtDTO tennisCourtDTO = new TennisCourtDTO();
        tennisCourtDTO.setName(tennisCourt.getName());
        tennisCourtDTO.setId(tennisCourt.getId());
        createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.now());
        createScheduleRequestDTO.setTennisCourtId(tennisCourt.getId());
        schedule = new Schedule();
        LocalDateTime startDateTime = LocalDateTime.now();
        schedule.setStartDateTime(startDateTime);
        schedule.setEndDateTime(startDateTime.plusHours(1L));
        schedule.setTennisCourt(tennisCourt);
        scheduleDTO = new ScheduleDTO();
        scheduleDTO.setTennisCourt(tennisCourtDTO);
        scheduleDTO.setStartDateTime(schedule.getStartDateTime());
        scheduleDTO.setEndDateTime(schedule.getEndDateTime());
    }

    @Test
    public void addSchedule() {
        Optional<TennisCourt> optionalTennisCourt = Optional.of(tennisCourt);
        Mockito.doReturn(optionalTennisCourt).when(tennisCourtRepository).findById(Mockito.anyLong());
        Mockito.doReturn(schedule).when(scheduleRepository).save(Mockito.any(Schedule.class));
        Mockito.doReturn(scheduleDTO).when(scheduleMapper).map(Mockito.any(Schedule.class));

        ScheduleDTO response = scheduleService.addSchedule(tennisCourt.getId(), createScheduleRequestDTO);

        Assert.assertEquals(response.getTennisCourt().getId(), createScheduleRequestDTO.getTennisCourtId());
        Assert.assertEquals(response.getStartDateTime().truncatedTo(ChronoUnit.MINUTES), createScheduleRequestDTO.getStartDateTime().truncatedTo(ChronoUnit.MINUTES));
        Assert.assertEquals(response.getEndDateTime().truncatedTo(ChronoUnit.MINUTES), createScheduleRequestDTO.getStartDateTime().plusHours(1L).truncatedTo(ChronoUnit.MINUTES));
    }
}
