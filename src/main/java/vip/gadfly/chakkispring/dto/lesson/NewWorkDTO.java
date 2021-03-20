package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "新建作业DTO", description = "新建作业")
public class NewWorkDTO {

    @ApiModelProperty(value = "作业名字", required = true)
    @NotBlank(message = "{lesson.work.name.not-null}")
    private String name;

    @ApiModelProperty(value = "作业信息")
    @Length(max = 255, message = "{group.info.length}")
    private String info;

    @ApiModelProperty(value = "班级id", required = true)
    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

    @ApiModelProperty(value = "限制文件大小")
    @Max(value = 20971520, message = "{lesson.work.file.size}")
    private Integer fileSize;

    @ApiModelProperty(value = "类型", required = true)
    @NotNull(message = "{lesson.work.type}")
    @Min(value = WorkTypeConstant.TYPE_CLASS, message = "{lesson.work.type}")
    @Max(value = WorkTypeConstant.TYPE_HOME, message = "{lesson.work.type}")
    private Integer type;

    @ApiModelProperty(value = "文件扩展名列表", required = true)
    @NotNull(message = "{lesson.work.extension.not-null}")
    private List<@NotBlank(message = "{lesson.work.extension.not-blank}") String> fileExtension;

    @ApiModelProperty(value = "结束时间")
    // @Future(message = "{lesson.work.end-time.not-null}")
    private Date endTime;

}
