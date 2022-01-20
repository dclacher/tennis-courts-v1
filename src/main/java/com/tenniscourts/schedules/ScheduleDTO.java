package com.tenniscourts.schedules;

import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Representation of a schedule")
public class ScheduleDTO {

    @ApiModelProperty(notes = "The ID of the schedule")
    private Long id;

    @ApiModelProperty(notes = "The tennis court that owns the schedule")
    private TennisCourtDTO tennisCourt;

    @NotNull
    @ApiModelProperty(notes = "The ID of the tennis court that owns the schedule")
    private Long tennisCourtId;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @NotNull
    @ApiModelProperty(notes = "The starting date and time of the schedule")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(notes = "The ending date and time of the schedule")
    private LocalDateTime endDateTime;

}
