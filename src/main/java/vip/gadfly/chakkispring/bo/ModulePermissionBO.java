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
public class ModulePermissionBO {

    private String module;

    private String permission;

}
