package vip.gadfly.chakkispring.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
public class NewSignDTO {

    @NotBlank(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Long classId;

    @NotBlank(message = "{lesson.sign.title.not-null}")
    private String title;

    @Min(value = 1, message = "{lesson.sign.end-time.not-null}")
    private Integer endMinutes;

}
