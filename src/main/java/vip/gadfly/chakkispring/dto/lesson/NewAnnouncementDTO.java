package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.gadfly.chakkispring.common.annotation.CharSize;
import vip.gadfly.chakkispring.common.annotation.MultimediaWordCount;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value="新建通知公告DTO", description="通知公告")
public class NewAnnouncementDTO {

    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "{lesson.announcement.title.not-null}")
    private String title;

    @ApiModelProperty(value = "正文富文本", required = true)
    @MultimediaWordCount(max = 15000, message = "{lesson.announcement.content.size}")
    @CharSize(max = 64000, message = "{lesson.announcement.content.size}")
    private String content;

    @ApiModelProperty(value = "班级Id", allowableValues = "range[1, infinity]", required = true)
    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

}
