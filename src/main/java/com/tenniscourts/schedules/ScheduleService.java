package com.tenniscourts.schedules;

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

        // The use case doesn't mention whether the code should validate already existing schedules for that court/time slot,
        // therefore it's simply overriding whatever it's already there in the database.

        // Create a Schedule entity
        Schedule schedule = new Schedule();
        schedule.setTennisCourt(tennisCourtMapper.map(tennisCourtDTO));
        schedule.setStartDateTime(createScheduleRequestDTO.getStartDateTime());
        schedule.setEndDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1L));

        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public ScheduleDTO updateSchedule(Long scheduleId, Reservation reservation) {
        ScheduleDTO scheduleDTO = findSchedule(scheduleId);
        Schedule schedule = scheduleMapper.map(scheduleDTO);
        schedule.addReservation(reservation);

        return scheduleMapper.map(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        //TODO: implement
        return null;
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        Optional<Schedule> schedule = Optional.of(scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found")));
        return scheduleMapper.map(schedule.get());
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
