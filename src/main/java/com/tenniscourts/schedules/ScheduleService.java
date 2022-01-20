package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.Reservation;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    private final TennisCourtService tennisCourtService;

    private final TennisCourtMapper tennisCourtMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        // Try to find the tennis court; tennis court service throws EntityNotFoundException otherwise
        TennisCourtDTO tennisCourtDTO = tennisCourtService.findTennisCourtById(tennisCourtId);

        // Validate whether the new schedule can be created (tennis court/date/time constraints)
        Schedule schedule =
                scheduleRepository.findFirstByTennisCourt_IdAndStartDateTimeBetween(tennisCourtId,
                                                                                    createScheduleRequestDTO.getStartDateTime()
                                                                                                            .minusMinutes(
                                                                                                                    59L),
                                                                                    createScheduleRequestDTO.getStartDateTime()
                                                                                                            .plusMinutes(
                                                                                                                    59L));
        if (schedule != null) {
            throw new AlreadyExistsEntityException(
                    "Schedule already exists or there's a time conflict with an existing schedule");
        }
        return scheduleMapper.map(scheduleRepository.save(
                Schedule.builder().tennisCourt(tennisCourtMapper.map(tennisCourtDTO))
                        .startDateTime(createScheduleRequestDTO.getStartDateTime())
                        .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L)).build()));
    }

    public ScheduleDTO updateSchedule(Long scheduleId, Reservation reservation) {
        ScheduleDTO scheduleDTO = findSchedule(scheduleId);
        Schedule schedule = scheduleMapper.map(scheduleDTO);
        schedule.addReservation(reservation);

        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return scheduleMapper.map(scheduleRepository.findByStartDateTimeBetween(startDateTime, endDateTime));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        Optional<Schedule> schedule = Optional.of(scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("Schedule not found")));
        return scheduleMapper.map(schedule.get());
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
