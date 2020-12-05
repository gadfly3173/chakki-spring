package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.GroupPermissionDO;

import java.util.List;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Repository
public interface GroupPermissionMapper extends BaseMapper<GroupPermissionDO> {

    int insertBatch(@Param("relations") List<GroupPermissionDO> relations);

    int deleteBatchByGroupIdAndPermissionId(@Param("groupId") Long groupId,
                                            @Param("permissionIds") List<Long> permissionIds);
}
