package vip.gadfly.chakkispring.dto.admin;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
    private Integer classId;

    @NotEmpty(message = "{class.user-ids.long-list}")
    private List<@Min(1) Integer> userIds;
}
