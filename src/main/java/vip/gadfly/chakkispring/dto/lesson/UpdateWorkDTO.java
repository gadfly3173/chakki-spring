package vip.gadfly.chakkispring.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import vip.gadfly.chakkispring.common.constant.WorkTypeConstant;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
public class UpdateWorkDTO {

    @NotNull(message = "{id.positive}")
    @Min(value = 1, message = "{id.positive}")
    private Integer id;

    @NotBlank(message = "{lesson.work.name.not-null}")
    private String name;

    @Length(max = 255, message = "{group.info.length}")
    private String info;

    @Max(value = 20971520, message = "{lesson.work.file.size}")
    private Integer fileSize;

    @Min(value = WorkTypeConstant.TYPE_CLASS, message = "{lesson.work.type}")
    @Max(value = WorkTypeConstant.TYPE_HOME, message = "{lesson.work.type}")
    private Integer type;

    @NotNull(message = "{lesson.work.extension.not-null}")
    private List<@NotBlank(message = "{lesson.work.extension.not-blank}") String> fileExtension;

    // @Future(message = "{lesson.work.end-time.not-null}")
    private Date endTime;

}
