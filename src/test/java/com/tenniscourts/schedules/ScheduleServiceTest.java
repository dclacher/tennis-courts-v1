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
import java.util.List;
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
    TennisCourtService tennisCourtService;

    @Mock
    TennisCourtMapper tennisCourtMapper;

    @Test
    public void addSchedule() {
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Arthur Ashe Stadium", 123L);

        Mockito.doReturn(tennisCourtDTO).when(tennisCourtService).findTennisCourtById(Mockito.anyLong());
        Mockito.doReturn(null)
               .when(scheduleRepository)
               .findFirstByTennisCourt_IdAndStartDateTimeBetween(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                                                                 Mockito.any(LocalDateTime.class));
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

    @Test(expected = com.tenniscourts.exceptions.AlreadyExistsEntityException.class)
    public void addSchedule_ScheduleAlreadyExists() {
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Arthur Ashe Stadium", 123L);
        Schedule schedule = TestDataFactory.createSchedule(LocalDateTime.now(), "Arthur Ashe Stadium");

        Mockito.doReturn(tennisCourtDTO).when(tennisCourtService).findTennisCourtById(Mockito.anyLong());
        Mockito.doReturn(schedule)
               .when(scheduleRepository)
               .findFirstByTennisCourt_IdAndStartDateTimeBetween(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                                                                 Mockito.any(LocalDateTime.class));

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.now());
        createScheduleRequestDTO.setTennisCourtId(tennisCourtDTO.getId());
        scheduleService.addSchedule(tennisCourtDTO.getId(), createScheduleRequestDTO);
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void addSchedule_TennisCourtNotFound() {
        Mockito.doThrow(new EntityNotFoundException("Tennis Court not found")).when(tennisCourtService)
               .findTennisCourtById(Mockito.anyLong());

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(LocalDateTime.now());
        createScheduleRequestDTO.setTennisCourtId(123L);
        scheduleService.addSchedule(123L, createScheduleRequestDTO);
    }

    @Test
    public void findSchedule() {
        Schedule schedule = TestDataFactory.createSchedule(LocalDateTime.now(), "Court 101");
        Optional<Schedule> scheduleOptional = Optional.of(schedule);
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Arthur Ashe Stadium", 131L);
        ScheduleDTO scheduleDTO = TestDataFactory.createScheduleDTO(101L, LocalDateTime.now(), tennisCourtDTO);

        Mockito.doReturn(scheduleOptional).when(scheduleRepository).findById(Mockito.anyLong());
        Mockito.doReturn(scheduleDTO).when(scheduleMapper).map(Mockito.any(Schedule.class));

        ScheduleDTO response = scheduleService.findSchedule(101L);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getTennisCourt().getName(), tennisCourtDTO.getName());
    }

    @Test
    public void findSchedulesByDates() {
        List<Schedule> schedules =
                List.of(TestDataFactory.createSchedule(
                        LocalDateTime.of(2021, 8, 31, 12, 0), "Court 8"));
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Court 8", 151L);
        List<ScheduleDTO> schedulesDTO =
                List.of(TestDataFactory.createScheduleDTO(75L, schedules.get(0).getStartDateTime(), tennisCourtDTO));

        Mockito.doReturn(schedules).when(scheduleRepository)
               .findByStartDateTimeBetween(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class));
        Mockito.doReturn(schedulesDTO).when(scheduleMapper).map(schedules);

        List<ScheduleDTO> response = scheduleService.findSchedulesByDates(schedules.get(0).getStartDateTime(),
                                             schedules.get(0).getStartDateTime().plusDays(1L));

        Assert.assertNotNull(response);
        Assert.assertEquals(1, response.size());
        Assert.assertEquals(response.get(0).getTennisCourtId(), tennisCourtDTO.getId());
    }

    @Test
    public void findSchedulesByTennisCourtId() {
        List<Schedule> schedules = List.of(TestDataFactory.createSchedule(LocalDateTime.now(), "Court 9"));
        TennisCourtDTO tennisCourtDTO = TestDataFactory.createTennisCourtDTO("Court 9", 161L);
        List<ScheduleDTO> schedulesDTO =
                List.of(TestDataFactory.createScheduleDTO(58L, LocalDateTime.now(), tennisCourtDTO));

        Mockito.doReturn(schedules).when(scheduleRepository)
               .findByTennisCourt_IdOrderByStartDateTime(Mockito.anyLong());
        Mockito.doReturn(schedulesDTO).when(scheduleMapper).map(schedules);

        List<ScheduleDTO> response = scheduleService.findSchedulesByTennisCourtId(161L);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.size(), schedulesDTO.size());
        Assert.assertEquals(response.get(0).getTennisCourtId(), tennisCourtDTO.getId());
    }
}
