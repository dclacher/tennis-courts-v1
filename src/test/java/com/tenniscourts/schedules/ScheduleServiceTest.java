package com.tenniscourts.schedules;

import com.tenniscourts.TestDataFactory;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.*;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    TennisCourtService tennisCourtService;

    @Mock
    TennisCourtMapper tennisCourtMapper;

    @Test
    public void addSchedule() {
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Arthur Ashe Stadium", 123L);
        Mockito.doReturn(tennisCourtDTO).when(tennisCourtService).findTennisCourtById(Mockito.anyLong());
        Mockito.doReturn(new TennisCourt()).when(tennisCourtMapper).map(Mockito.any(TennisCourtDTO.class));
        Mockito.doReturn(TestDataFactory.createSchedule(LocalDateTime.now(), "Arthur Ashe Stadium"))
               .when(scheduleRepository).save(Mockito.any(Schedule.class));
        Mockito.doReturn(TestDataFactory.createScheduleDTO(444L, LocalDateTime.now(), tennisCourtDTO))
               .when(scheduleMapper)
               .map(Mockito.any(Schedule.class));

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.now());
        createScheduleRequestDTO.setTennisCourtId(tennisCourtDTO.getId());
        ScheduleDTO response = scheduleService.addSchedule(tennisCourtDTO.getId(), createScheduleRequestDTO);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getTennisCourt().getId(), createScheduleRequestDTO.getTennisCourtId());
        Assert.assertEquals(response.getStartDateTime().truncatedTo(ChronoUnit.MINUTES),
                            createScheduleRequestDTO.getStartDateTime().truncatedTo(ChronoUnit.MINUTES));
        Assert.assertEquals(response.getEndDateTime().truncatedTo(ChronoUnit.MINUTES),
                            createScheduleRequestDTO.getStartDateTime().plusHours(1L).truncatedTo(ChronoUnit.MINUTES));
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void addScheduleTennisCourtNotFound() {
        Mockito.doThrow(new EntityNotFoundException("Tennis Court not found")).when(tennisCourtService)
               .findTennisCourtById(Mockito.anyLong());

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.now());
        createScheduleRequestDTO.setTennisCourtId(123L);
        scheduleService.addSchedule(123L, createScheduleRequestDTO);
    }
}
