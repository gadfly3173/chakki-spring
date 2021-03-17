package vip.gadfly.chakkispring.dto.lesson;

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
public class NewQuestionnaire {

    @Length(min = 1, max = 60, message = "{lesson.questionnaire.title.not-null}")
    private String title;

    @Length(max = 255, message = "{lesson.questionnaire.info.length}")
    private String info;

    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

    @Valid
    @NotNull(message = "{lesson.questionnaire.length}")
    @Size(min = 1, max = 99, message = "{lesson.questionnaire.length}")
    private List<NewQuestion> questions;

    private Date endTime;
}
