package vip.gadfly.chakkispring.dto.lesson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.gadfly.chakkispring.common.annotation.CharSize;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
public class NewAnnouncementDTO {

    @NotBlank(message = "{lesson.announcement.title.not-null}")
    private String title;

    @CharSize(max = 64000, message = "{lesson.announcement.content.size}")
    private String content;

    @NotNull(message = "{class.id.not-null}")
    @Min(value = 1, message = "{class.id.not-null}")
    private Integer classId;

}
