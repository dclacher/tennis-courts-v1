package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

    Schedule findFirstByTennisCourt_IdAndStartDateTimeBetween(Long id, LocalDateTime dateTime1,
                                                              LocalDateTime dateTime2);

    List<Schedule> findByStartDateTimeBetween(LocalDateTime dateTime1, LocalDateTime dateTime2);
}