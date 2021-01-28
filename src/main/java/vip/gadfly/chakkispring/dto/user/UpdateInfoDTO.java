package vip.gadfly.chakkispring.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@NoArgsConstructor
@Data
public class UpdateInfoDTO {

    @Email(message = "{email}")
    private String email;

    @Length(min = 2, max = 10, message = "{nickname.size}")
    private String nickname;

    @Length(min = 2, max = 10, message = "{username.size}")
    private String username;

    @Length(max = 500, message = "{avatar.size}")
    private String avatar;
}
