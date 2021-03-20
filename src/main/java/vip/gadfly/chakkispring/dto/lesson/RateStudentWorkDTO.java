package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "作业打分DTO", description = "作业打分")
public class RateStudentWorkDTO {

    @ApiModelProperty(value = "分数", required = true)
    @NotNull(message = "{lesson.work.rate.not-null}")
    @Min(value = 0, message = "{lesson.work.rate.not-null}")
    @Max(value = 10, message = "{lesson.work.rate.not-null}")
    private Integer rate;

}
