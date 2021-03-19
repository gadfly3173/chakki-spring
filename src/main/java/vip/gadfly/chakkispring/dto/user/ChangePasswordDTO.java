package vip.gadfly.chakkispring.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@NoArgsConstructor
@EqualField(srcField = "newPassword", dstField = "confirmPassword", message = "{password.equal-field}")
@ApiModel(value = "修改密码DTO", description = "修改密码")
public class ChangePasswordDTO {

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "{new.password.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{new.password.pattern}")
    private String newPassword;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "{confirm.password.not-blank}")
    private String confirmPassword;

    @ApiModelProperty(value = "旧密码", required = true)
    @NotBlank(message = "{old.password.not-blank}")
    private String oldPassword;
}
