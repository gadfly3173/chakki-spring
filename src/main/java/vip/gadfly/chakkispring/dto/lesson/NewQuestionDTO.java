package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "新建问题DTO", description = "问题")
public class NewQuestionDTO {

    @ApiModelProperty(value = "标题", required = true)
    @Length(min = 1, max = 60, message = "{lesson.questionnaire.question.title.not-null}")
    private String title;

    @ApiModelProperty(value = "类型", allowableValues = "1,2", required = true)
    @NotNull(message = "{lesson.questionnaire.question.type.not-null}")
    @Min(value = 1, message = "{lesson.questionnaire.question.type.not-null}")
    @Max(value = 2, message = "{lesson.questionnaire.question.type.not-null}")
    private Integer type;

    @ApiModelProperty(value = "上限，目前用于选择题", allowableValues = "range[1,10]")
    @Min(value = 1, message = "{lesson.questionnaire.question.limit.not-null}")
    @Max(value = 10, message = "{lesson.questionnaire.question.limit.not-null}")
    private Integer limitMax;

    @ApiModelProperty(value = "选项列表")
    @Valid
    @Size(max = 10, message = "{lesson.questionnaire.question.option.limit}")
    private List<NewOptionDTO> options;

}
