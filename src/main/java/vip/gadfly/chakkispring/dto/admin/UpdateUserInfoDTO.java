package vip.gadfly.chakkispring.dto.admin;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateUserInfoDTO {

    @NotEmpty(message = "{group.ids.long-list}")
    private List<@Min(1) Integer> groupIds;

    @NotBlank(message = "{user.username.not-blank}")
    @Size(max = 10, min = 2, message = "{user.username.size}")
    private String username;

    @NotBlank(message = "{user.nickname.not-blank}")
    @Size(max = 10, min = 2, message = "{user.nickname.size}")
    private String nickname;
}
