package vip.gadfly.chakkispring.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateUserInfoDTO {

    // @LongList(min = 1, message = "{group.ids.long-list}")
    private List<Integer> groupIds;

    @NotBlank(message = "{user.username.not-blank}")
    @Size(max = 10, min = 2, message = "{user.username.size}")
    private String username;

    @NotBlank(message = "{user.nickname.not-blank}")
    @Size(max = 10, min = 2, message = "{user.nickname.size}")
    private String nickname;
}
