package vip.gadfly.chakkispring.dto.lesson;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class NewWorkDTO {

    @NotBlank(message = "{lesson.work.name.not-null}")
    private String name;

    @Length(max = 255, message = "{group.info.length}")
    private String info;

    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

    @Max(value = 20971520, message = "{lesson.work.file.size}")
    private Integer fileSize;

    @Min(value = WorkTypeConstant.TYPE_CLASS, message = "{lesson.work.type}")
    @Max(value = WorkTypeConstant.TYPE_HOME, message = "{lesson.work.type}")
    private Integer type;

    @NotNull(message = "lesson.work.extension.not-null")
    private List<@NotBlank(message = "lesson.work.extension.not-blank") String> fileExtension;

    // @Future(message = "{lesson.work.end-time.not-null}")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

}
