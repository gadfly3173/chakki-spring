package vip.gadfly.chakkispring.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import vip.gadfly.chakkispring.bo.ModulePermissionBO;
import vip.gadfly.chakkispring.model.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 用户 + 权限 view object
 */
@Data
@ApiModel(value = "用户权限VO", description = "用户权限")
public class UserPermissionVO {

    private Integer id;

    private String nickname;

    private String avatar;

    private Boolean admin;

    private String email;

    private List<Map<String, List<ModulePermissionBO>>> permissions;

    public UserPermissionVO() {
    }

    public UserPermissionVO(UserDO userDO, List<Map<String, List<ModulePermissionBO>>> permissions) {
        BeanUtils.copyProperties(userDO, this);
        this.permissions = permissions;
    }

    public UserPermissionVO(UserDO userDO) {
        BeanUtils.copyProperties(userDO, this);
    }
}
