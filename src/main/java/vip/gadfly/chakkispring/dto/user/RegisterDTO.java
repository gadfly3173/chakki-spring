package vip.gadfly.chakkispring.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualField(srcField = "password", dstField = "confirmPassword", message = "{password.equal-field}")
public class RegisterDTO {

    @NotBlank(message = "{username.not-blank}")
    @Size(min = 2, max = 10, message = "{username.size}")
    private String username;

    @Size(min = 2, max = 10, message = "{nickname.length}")
    private String nickname;

    // @LongList(allowBlank = true, message = "{group.ids.long-list}")
    private List<Integer> groupIds;

    @Email(message = "{email}")
    private String email;

    @NotBlank(message = "{new.password.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{new.password.pattern}")
    private String password;

    @NotBlank(message = "{confirm.password.not-blank}")
    private String confirmPassword;
}
