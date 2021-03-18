package vip.gadfly.chakkispring.dto.lesson;

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
public class NewQuestionDTO {

    @Length(min = 1, max = 60, message = "{lesson.questionnaire.question.title.not-null}")
    private String title;

    @NotNull(message = "{lesson.questionnaire.question.type.not-null}")
    @Min(value = 1, message = "{lesson.questionnaire.question.type.not-null}")
    @Max(value = 2, message = "{lesson.questionnaire.question.type.not-null}")
    private Integer type;

    @Min(value = 1, message = "{lesson.questionnaire.question.limit.not-null}")
    @Max(value = 10, message = "{lesson.questionnaire.question.limit.not-null}")
    private Integer limit;

    @Valid
    @Size(max = 10, message = "{lesson.questionnaire.question.option.limit}")
    private List<NewOptionDTO> options;

}
