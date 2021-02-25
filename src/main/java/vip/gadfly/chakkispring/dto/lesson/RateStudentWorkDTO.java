package vip.gadfly.chakkispring.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
public class RateStudentWorkDTO {

    @NotNull(message = "{lesson.work.rate.not-null}")
    @Min(value = 0, message = "{lesson.work.rate.not-null}")
    @Max(value = 10, message = "{lesson.work.rate.not-null}")
    private Integer rate;

}
