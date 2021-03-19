package vip.gadfly.chakkispring.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "新建问题DTO", description = "问题")
public class LoginDTO {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "{username.not-blank}")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "{password.new.not-blank}")
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "{captcha.not-blank}")
    private String captcha;
}
