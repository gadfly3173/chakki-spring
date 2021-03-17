package vip.gadfly.chakkispring.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Gadfly
 */


@Setter
@Getter
@NoArgsConstructor
public class NewQuestion {

    @Length(min = 1, max = 60, message = "{lesson.questionnaire.question.title.not-null}")
    private String title;

    @NotNull(message = "{lesson.questionnaire.question.order.not-null}")
    @Min(value = 1, message = "{lesson.questionnaire.question.order.not-null}")
    @Max(value = 99, message = "{lesson.questionnaire.question.order.not-null}")
    private Integer order;

    @NotNull(message = "{lesson.questionnaire.question.type.not-null}")
    @Min(value = 1, message = "{lesson.questionnaire.question.type.not-null}")
    @Max(value = 2, message = "{lesson.questionnaire.question.type.not-null}")
    private Integer type;

    @Min(value = 1, message = "{lesson.questionnaire.question.limit.not-null}")
    @Max(value = 10, message = "{lesson.questionnaire.question.limit.not-null}")
    private Integer limit;

    @Valid
    private List<NewOption> options;

}
