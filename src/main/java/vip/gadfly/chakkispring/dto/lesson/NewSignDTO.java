package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "新建签到DTO", description = "新建签到")
public class NewSignDTO {

    @ApiModelProperty(value = "班级id", name = "class_id", required = true)
    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

    @ApiModelProperty(value = "签到标题", required = true)
    @NotBlank(message = "{lesson.sign.title.not-null}")
    private String title;

    @ApiModelProperty(value = "结束分钟", name = "end_minutes", required = true)
    @NotNull(message = "{lesson.sign.end-time.not-null}")
    @Min(value = 1, message = "{lesson.sign.end-time.not-null}")
    private Integer endMinutes;

}
