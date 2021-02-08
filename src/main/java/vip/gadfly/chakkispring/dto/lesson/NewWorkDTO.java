package vip.gadfly.chakkispring.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.gadfly.chakkispring.common.constant.WorkTypeConstant;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
public class NewWorkDTO {

    @NotBlank(message = "{lesson.work.name.not-null}")
    private String name;

    private String info;

    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

    @Max(value = 10, message = "{lesson.work.file.num}")
    private Integer fileNum;

    @Max(value = 20971520, message = "{lesson.work.file.size}")
    private Integer fileSize;

    @Min(value = WorkTypeConstant.TYPE_CLASS, message = "{lesson.work.type}")
    @Max(value = WorkTypeConstant.TYPE_HOME, message = "{lesson.work.type}")
    private Integer type;

    @NotNull(message = "{lesson.sign.end-time.not-null}")
    @Min(value = 1, message = "{lesson.sign.end-time.not-null}")
    private Integer endTime;

}
