package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author Gadfly
 */


@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "新建问卷DTO", description = "问卷")
public class NewQuestionnaireDTO {

    @ApiModelProperty(value = "标题", required = true)
    @Length(min = 1, max = 60, message = "{lesson.questionnaire.title.not-null}")
    private String title;

    @ApiModelProperty(value = "简介")
    @Length(max = 255, message = "{lesson.questionnaire.info.length}")
    private String info;

    @ApiModelProperty(value = "班级id", name = "class_id", required = true)
    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

    @ApiModelProperty(value = "问题列表", required = true)
    @Valid
    @NotNull(message = "{lesson.questionnaire.length}")
    @Size(min = 1, max = 99, message = "{lesson.questionnaire.length}")
    private List<NewQuestionDTO> questions;

    @ApiModelProperty(value = "结束时间", name = "end_time")
    private Date endTime;
}
