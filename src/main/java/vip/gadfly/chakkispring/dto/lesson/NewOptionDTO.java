package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "新建选项DTO", description = "选项")
public class NewOptionDTO {

    @ApiModelProperty(value = "标题")
    @Length(min = 1, max = 60, message = "{lesson.questionnaire.question.option.title.not-null}")
    private String title;

}
