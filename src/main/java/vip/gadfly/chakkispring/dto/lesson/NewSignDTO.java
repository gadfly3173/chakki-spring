package vip.gadfly.chakkispring.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
public class NewSignDTO {

    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Long classId;

    @NotBlank(message = "{lesson.sign.title.not-null}")
    private String title;

    @NotNull(message = "{lesson.sign.end-time.not-null}")
    @Min(value = 1, message = "{lesson.sign.end-time.not-null}")
    private Integer endMinutes;

}
