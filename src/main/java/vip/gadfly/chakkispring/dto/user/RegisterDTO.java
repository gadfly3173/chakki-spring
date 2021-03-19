package vip.gadfly.chakkispring.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="注册用户DTO", description="注册")
public class RegisterDTO {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "{username.not-blank}")
    @Size(min = 2, max = 10, message = "{username.size}")
    private String username;

    @ApiModelProperty(value = "昵称")
    @Size(min = 2, max = 10, message = "{nickname.length}")
    private String nickname;

    @ApiModelProperty(value = "权限组id列表")
    // @LongList(allowBlank = true, message = "{group.ids.long-list}")
    private List<Integer> groupIds;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "{email}")
    private String email;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "{new.password.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{new.password.pattern}")
    private String password;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "{confirm.password.not-blank}")
    private String confirmPassword;
}
