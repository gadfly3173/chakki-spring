package vip.gadfly.chakkispring.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
public class DispatchPermissionListDTO {

    @Positive(message = "{group.id.positive}")
    @NotNull(message = "{group.id.not-null}")
    private Integer groupId;

    // @LongList(message = "{permission.ids.long-list}")
    private List<Integer> permissionIds;
}
