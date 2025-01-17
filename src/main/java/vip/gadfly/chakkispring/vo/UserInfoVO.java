package vip.gadfly.chakkispring.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import vip.gadfly.chakkispring.model.UserDO;

import java.util.List;

/**
 * 用户信息 view object
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private Integer id;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 分组
     */
    private List groups;

    public UserInfoVO(UserDO user, List groups) {
        BeanUtils.copyProperties(user, this);
        this.groups = groups;
    }
}
