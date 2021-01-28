package vip.gadfly.chakkispring.dto.admin;

import lombok.Data;
import vip.gadfly.chakkispring.common.constant.TeacherLevelConstant;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author Gadfly
 */

@Data
public class AddTeacherClassDTO {
    @Positive(message = "{class.id.positive}")
    @NotNull(message = "{class.id.not-null}")
    private Integer classId;

    @Min(value = TeacherLevelConstant.MAIN_LEVEL, message = "{teacher.level.id.positive}")
    @Max(value = TeacherLevelConstant.SUB_LEVEL, message = "{teacher.level.id.positive}")
    @NotNull(message = "{teacher.level.not-null}")
    private Integer level;

    @Size(min = 1, message = "{class.user-ids.long-list}")
    // @LongList(message = "{class.user-ids.long-list}")
    private List<Integer> userIds;
}
