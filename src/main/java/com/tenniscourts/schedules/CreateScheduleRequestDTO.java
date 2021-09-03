package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleRequestDTO {

    @NotNull
    private Long tennisCourtId;

    // The application allows invalid dates such as 2021-09-31T22:00 (it creates on 2021-09-30). I tried to fix it by setting
    // lenient to false, but it didn't work. Maybe a custom validator would be the only solution (using @Valid with the @RequestBody)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", lenient = OptBoolean.FALSE)
    @NotNull
    private LocalDateTime startDateTime;

}
