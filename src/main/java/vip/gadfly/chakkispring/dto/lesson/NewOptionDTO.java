package vip.gadfly.chakkispring.dto.lesson;

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
public class NewOptionDTO {

    @Length(min = 1, max = 60, message = "{lesson.questionnaire.question.option.title.not-null}")
    private String title;

}
