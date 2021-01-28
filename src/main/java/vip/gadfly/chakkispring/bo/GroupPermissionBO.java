package vip.gadfly.chakkispring.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import vip.gadfly.chakkispring.model.GroupDO;
import vip.gadfly.chakkispring.model.PermissionDO;

import java.util.List;

/**
 * @author 24273
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPermissionBO {
    private Integer id;

    private String name;

    private String info;

    private List<PermissionDO> permissions;

    public GroupPermissionBO(GroupDO group, List<PermissionDO> permissions) {
        BeanUtils.copyProperties(group, this);
        this.permissions = permissions;
    }
}
