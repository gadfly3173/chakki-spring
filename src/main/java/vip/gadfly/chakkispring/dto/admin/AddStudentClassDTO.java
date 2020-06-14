package vip.gadfly.chakkispring.dto.admin;

import io.github.talelin.autoconfigure.validator.LongList;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Gadfly
 */

@Data
public class AddStudentClassDTO {
    @Positive(message = "{class.id.positive}")
    @NotNull(message = "{class.id.not-null}")
    private Long classId;

    @LongList(message = "{class.user-ids.long-list}")
    private List<Long> userIds;
}
