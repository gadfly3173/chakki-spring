package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.gadfly.chakkispring.bo.ModulePermissionBO;
import vip.gadfly.chakkispring.model.PermissionDO;

import java.util.List;
import java.util.Map;

/**
 * @author pedro
 * @since 2019-11-30
 */
public interface PermissionService extends IService<PermissionDO> {

    /**
     * 通过分组id得到分组的权限
     *
     * @param groupId 分组id
     * @return 权限
     */
    List<PermissionDO> getPermissionByGroupId(Integer groupId);

    /**
     * 通过分组id得到分组的权限
     *
     * @param groupIds 分组id
     * @return 权限
     */
    List<PermissionDO> getPermissionByGroupIds(List<Integer> groupIds);

    /**
     * 通过分组id得到分组的权限与分组id的映射
     *
     * @param groupIds 分组id
     * @return 权限map
     */
    Map<Integer, List<PermissionDO>> getPermissionMapByGroupIds(List<Integer> groupIds);

    /**
     * 将权限结构化
     *
     * @param permissions 权限
     * @return 结构化的权限
     */
    List<Map<String, List<ModulePermissionBO>>> structuringPermissions(List<PermissionDO> permissions);

    /**
     * 将权限简单地结构化
     *
     * @param permissions 权限
     * @return 结构化的权限
     */
    Map<String, List<String>> structuringPermissionsSimply(List<PermissionDO> permissions);

    /**
     * 通过分组id和权限模块得到分组的权限与分组id的映射
     *
     * @param groupIds 分组id
     * @param module   权限模块
     * @return 权限map
     */
    List<PermissionDO> getPermissionByGroupIdsAndModule(List<Integer> groupIds, String module);
}
