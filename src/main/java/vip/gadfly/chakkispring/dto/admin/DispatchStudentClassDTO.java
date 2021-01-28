package vip.gadfly.chakkispring.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Gadfly
 */

@Data
public class DispatchStudentClassDTO {
    @Positive(message = "{class.id.positive}")
    @NotNull(message = "{class.id.not-null}")
    private Integer userId;

    // @LongList(message = "{class.ids.long-list}")
    private List<Integer> classIds;
}
