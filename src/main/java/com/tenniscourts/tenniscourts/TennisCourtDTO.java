package com.tenniscourts.tenniscourts;

import com.tenniscourts.schedules.ScheduleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Representation of a tennis court")
public class TennisCourtDTO {

    private Long id;

    @NotNull
    @ApiModelProperty(notes = "The name of the tennis court")
    private String name;

    @ApiModelProperty(notes = "The list of schedules for the tennis court")
    private List<ScheduleDTO> tennisCourtSchedules;

}
