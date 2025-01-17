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
@ApiModel(value = "更新作业DTO", description = "更新作业")
public class UpdateWorkDTO {

    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "{id.positive}")
    @Min(value = 1, message = "{id.positive}")
    private Integer id;

    @ApiModelProperty(value = "作业名字", required = true)
    @NotBlank(message = "{lesson.work.name.not-null}")
    private String name;

    @ApiModelProperty(value = "作业简介")
    @Length(max = 255, message = "{group.info.length}")
    private String info;

    @ApiModelProperty(value = "限制文件大小", name = "file_size")
    @Max(value = 20971520, message = "{lesson.work.file.size}")
    private Integer fileSize;

    @ApiModelProperty(value = "类型", required = true)
    @NotNull(message = "{lesson.work.type}")
    @Min(value = WorkTypeConstant.TYPE_CLASS, message = "{lesson.work.type}")
    @Max(value = WorkTypeConstant.TYPE_HOME, message = "{lesson.work.type}")
    private Integer type;

    @ApiModelProperty(value = "文件扩展名列表", name = "file_extension", required = true)
    @NotNull(message = "{lesson.work.extension.not-null}")
    private List<@NotBlank(message = "{lesson.work.extension.not-blank}") String> fileExtension;

    @ApiModelProperty(value = "结束时间", name = "end_time")
    // @Future(message = "{lesson.work.end-time.not-null}")
    private Date endTime;

}
