package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/schedules")
@Api(tags = "Schedules Management")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ApiOperation(value = "addScheduleTennisCourt", tags = "Add schedule to tennis court",
                  response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request"),
            @ApiResponse(code = 409, message = "Schedule already exists in the system")})
    public ResponseEntity<Void> addScheduleTennisCourt(
            @ApiParam(value = "Create schedule request", required = true) @RequestBody
                    CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(
                scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO)
                               .getId())).build();
    }

    @GetMapping("/find-by-dates")
    @ApiOperation(value = "findSchedulesByDates", tags = "Find schedules by dates", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request")})
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(
            @ApiParam(value = "Start date", required = true) @RequestParam("startDate") LocalDate startDate,
            @ApiParam(value = "End date", required = true) @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                                                                      LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "findByScheduleId", tags = "Find schedule by ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request"),
            @ApiResponse(code = 404, message = "Schedule not found")})
    public ResponseEntity<ScheduleDTO> findByScheduleId(
            @ApiParam(value = "Schedule ID", required = true) @PathVariable(name = "id") Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }

    @GetMapping("/find-by-tennis-court-id")
    @ApiOperation(value = "findSchedulesByTennisCourtId", tags = "Find schedules by tennis court ID",
                  response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request")})
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByTennisCourtId(
            @ApiParam(value = "Tennis court ID", required = true) @RequestParam("id") Long tennisCourtId) {
        return ResponseEntity.ok(scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
    }
}
