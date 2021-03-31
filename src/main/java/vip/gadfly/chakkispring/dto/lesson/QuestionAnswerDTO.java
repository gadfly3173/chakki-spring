package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "学生回答问卷的问题DTO", description = "问题")
public class QuestionAnswerDTO {

    @ApiModelProperty(value = "简答回答")
    @Length(max = 255, message = "{lesson.questionnaire.question.answer.length}")
    private String answer;

    @ApiModelProperty(value = "单选选项", name = "single_option_id")
    private Integer singleOptionId;

    @ApiModelProperty(value = "多选选项列表", name = "multi_option_id")
    @Size(max = 10, message = "{lesson.questionnaire.question.option.limit}")
    private List<Integer> multiOptionId;

}
