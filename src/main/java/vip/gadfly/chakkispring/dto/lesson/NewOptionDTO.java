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
public class NewOptionDTO {

    @Length(min = 1, max = 60, message = "{lesson.questionnaire.question.option.title.not-null}")
    private String title;

    @NotNull(message = "{lesson.questionnaire.question.option.order.not-null}")
    @Min(value = 1, message = "{lesson.questionnaire.question.option.order.not-null}")
    @Max(value = 10, message = "{lesson.questionnaire.question.option.order.not-null}")
    private Integer order;

}
