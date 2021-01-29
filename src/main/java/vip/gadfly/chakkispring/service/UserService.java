package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.dto.user.BatchRegisterDTO;
import vip.gadfly.chakkispring.dto.user.ChangePasswordDTO;
import vip.gadfly.chakkispring.dto.user.RegisterDTO;
import vip.gadfly.chakkispring.dto.user.UpdateInfoDTO;
import vip.gadfly.chakkispring.model.GroupDO;
import vip.gadfly.chakkispring.model.PermissionDO;
import vip.gadfly.chakkispring.model.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 用户业务
 *
 * @author pedro
 * @since 2019-11-30
 */
public interface UserService extends IService<UserDO> {

    /**
     * 新建用户
     *
     * @param validator 新建用户校验器
     * @return 被创建的用户
     */
    UserDO createUser(RegisterDTO validator);

    /**
     * 批量新建用户
     *
     * @param validator 新建用户校验器
     * @return 是否成功
     */
    boolean createBatchUser(BatchRegisterDTO validator);

    /**
     * 更新用户
     *
     * @param validator 更新用户信息用户校验器
     * @return 被更新的用户
     */
    UserDO updateUserInfo(UpdateInfoDTO validator);

    boolean adminUpdateUserInfo(Integer id, String username, String nickname);

    /**
     * 修改用户密码
     *
     * @param validator 修改密码校验器
     * @return 被修改密码的用户
     */
    UserDO changeUserPassword(ChangePasswordDTO validator);

    /**
     * 获得用户的所有分组
     *
     * @param userId 用户id
     * @return 所有分组
     */
    List<GroupDO> getUserGroups(Integer userId);

    /**
     * 获得用户所有权限
     *
     * @param userId 用户id
     * @return 权限
     */
    List<Map<String, List<Map<String, String>>>> getStructuralUserPermissions(Integer userId);

    /**
     * 获得用户所有权限
     *
     * @param userId 用户id
     * @return 权限
     */
    List<PermissionDO> getUserPermissions(Integer userId);

    /**
     * 获得用户在模块下的所有权限
     *
     * @param userId 用户id
     * @param module 权限模块
     * @return 权限
     */
    List<PermissionDO> getUserPermissionsByModule(Integer userId, String module);

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    UserDO getUserByUsername(String username);

    /**
     * 根据用户名检查用户是否存在
     *
     * @param username 用户名
     * @return true代表存在
     */
    boolean checkUserExistByUsername(String username);

    /**
     * 根据用户名检查用户是否存在
     *
     * @param email 邮箱
     * @return true代表存在
     */
    boolean checkUserExistByEmail(String email);

    /**
     * 根据用户id检查用户是否存在
     *
     * @param id 用户名
     * @return true代表存在
     */
    boolean checkUserExistById(Integer id);

    /**
     * 根据分组id分页获取用户数据
     *
     * @param pager   分页
     * @param groupId 分组id
     * @return 数据页
     */
    IPage<UserDO> getUserPageByGroupId(Page<UserDO> pager, Integer groupId);

    /**
     * 根据班级id分页获取用户数据
     *
     * @param pager   分页
     * @param classId 班级id
     * @return 数据页
     */
    IPage<UserDO> getUserPageByClassId(Page<UserDO> pager, Integer classId);

    /**
     * 根据班级id分页获取非此班级学生数据
     *
     * @param pager   分页
     * @param classId 班级id
     * @return 数据页
     */
    IPage<UserDO> getFreshUserPageByClassId(Page<UserDO> pager, Integer classId);

    /**
     * 根据班级id分页和名字获取非此班级学生数据
     *
     * @param pager   分页
     * @param classId 班级id
     * @param name    名字
     * @return 数据页
     */
    IPage<UserDO> getFreshUserPageByClassIdAndName(Page<UserDO> pager, Integer classId, String name);

    /**
     * 根据班级id分页和名字获取非此班级教师数据
     *
     * @param pager   分页
     * @param classId 班级id
     * @param name    名字
     * @return 数据页
     */
    IPage<UserDO> getFreshTeacherPageByClassIdAndName(Page<UserDO> pager, Integer classId, String name);

    /**
     * 获取超级管理员的id
     *
     * @return 超级管理员的id
     */
    Integer getRootUserId();
}
