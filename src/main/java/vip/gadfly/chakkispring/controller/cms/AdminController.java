package vip.gadfly.chakkispring.controller.cms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.AdminRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.bo.GroupPermissionBO;
import vip.gadfly.chakkispring.common.util.PageUtil;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.admin.*;
import vip.gadfly.chakkispring.model.GroupDO;
import vip.gadfly.chakkispring.model.PermissionDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.AdminService;
import vip.gadfly.chakkispring.service.GoogleAuthenticatorService;
import vip.gadfly.chakkispring.service.GroupService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import vip.gadfly.chakkispring.vo.UserInfoVO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lin on 2019/06/12.
 * License MIT
 */

@RestController
@RequestMapping("/cms/admin")
@PermissionModule(value = "管理员")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GoogleAuthenticatorService googleAuthenticatorService;

    @GetMapping("/permission")
    @AdminRequired
    @PermissionMeta(value = "查询所有可分配的权限", mount = false)
    public Map<String, List<PermissionDO>> getAllPermissions() {
        return adminService.getAllStructuralPermissions();
    }

    @GetMapping("/users")
    @AdminRequired
    @PermissionMeta(value = "查询所有用户", mount = false)
    public PageResponseVO<UserInfoVO> getUsers(
            @RequestParam(name = "group_id", required = false)
            @Min(value = 1, message = "{group.id.positive}") Integer groupId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{page.count.min}")
            @Max(value = 30, message = "{page.count.max}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page.number.min}") Integer page) {
        IPage<UserDO> iPage = adminService.getUserPageByGroupId(groupId, count, page);
        List<UserInfoVO> userInfos = iPage.getRecords().stream().map(user -> {
            List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
            return new UserInfoVO(user, groups);
        }).collect(Collectors.toList());
        return PageUtil.build(iPage, userInfos);
    }

    @PutMapping("/user/{id}/password")
    @AdminRequired
    @PermissionMeta(value = "修改用户密码", mount = false)
    public UnifyResponseVO<String> changeUserPassword(@PathVariable @Positive(message = "{id.positive}") Integer id,
                                                      @RequestBody @Validated ResetPasswordDTO validator) {
        adminService.changeUserPassword(id, validator);
        return ResponseUtil.generateUnifyResponse(2);
    }

    @DeleteMapping("/user/{id}")
    @AdminRequired
    @PermissionMeta(value = "删除用户", mount = false)
    public UnifyResponseVO<String> deleteUser(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        adminService.deleteUser(id);
        return ResponseUtil.generateUnifyResponse(3);
    }

    @DeleteMapping("/user/mfa/{id}")
    @AdminRequired
    @PermissionMeta(value = "删除用户两步验证", mount = false)
    public UnifyResponseVO<String> deleteUserMFA(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        googleAuthenticatorService.cancelMFA(id);
        return ResponseUtil.generateUnifyResponse(34);
    }

    @PutMapping("/user/{id}")
    @AdminRequired
    @PermissionMeta(value = "管理员更新用户信息", mount = false)
    public UnifyResponseVO<String> updateUser(@PathVariable @Positive(message = "{id.positive}") Integer id,
                                              @RequestBody @Validated UpdateUserInfoDTO validator) {
        adminService.updateUserInfo(id, validator);
        return ResponseUtil.generateUnifyResponse(4);
    }

    @GetMapping("/group")
    @AdminRequired
    @PermissionMeta(value = "查询所有权限组及其权限", mount = false)
    public PageResponseVO<GroupDO> getGroups(
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{page.count.min}")
            @Max(value = 30, message = "{page.count.max}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page.number.min}") Integer page) {
        IPage<GroupDO> iPage = adminService.getGroupPage(page, count);
        return PageUtil.build(iPage);
    }

    @GetMapping("/group/all")
    @PermissionMeta(value = "查询所有权限组", mount = false)
    public List<GroupDO> getAllGroup() {
        List<GroupDO> groups = adminService.getAllGroups();
        return groups;
    }

    @GetMapping("/group/{id}")
    @AdminRequired
    @PermissionMeta(value = "查询一个权限组及其权限", mount = false)
    public GroupPermissionBO getGroup(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return adminService.getGroup(id);
    }

    @PostMapping("/group")
    @AdminRequired
    @PermissionMeta(value = "新建权限组", mount = false)
    public UnifyResponseVO<String> createGroup(@RequestBody @Validated NewGroupDTO validator) {
        adminService.createGroup(validator);
        return ResponseUtil.generateUnifyResponse(13);
    }

    @PutMapping("/group/{id}")
    @AdminRequired
    @PermissionMeta(value = "更新一个权限组", mount = false)
    public UnifyResponseVO<String> updateGroup(@PathVariable @Positive(message = "{id.positive}") Integer id,
                                               @RequestBody @Validated UpdateGroupDTO validator) {
        adminService.updateGroup(id, validator);
        return ResponseUtil.generateUnifyResponse(5);
    }

    @DeleteMapping("/group/{id}")
    @AdminRequired
    @PermissionMeta(value = "删除一个权限组", mount = false)
    public UnifyResponseVO<String> deleteGroup(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        adminService.deleteGroup(id);
        return ResponseUtil.generateUnifyResponse(6);
    }

    @PostMapping("/permission/dispatch")
    @AdminRequired
    @PermissionMeta(value = "分配单个权限", mount = false)
    public UnifyResponseVO<String> dispatchPermission(@RequestBody @Validated DispatchPermissionDTO validator) {
        adminService.dispatchPermission(validator);
        return ResponseUtil.generateUnifyResponse(7);
    }

    @PostMapping("/permission/dispatch/batch")
    @AdminRequired
    @PermissionMeta(value = "分配多个权限", mount = false)
    public UnifyResponseVO<String> dispatchPermissions(@RequestBody @Validated DispatchPermissionListDTO validator) {
        adminService.dispatchPermissions(validator);
        return ResponseUtil.generateUnifyResponse(7);
    }

    @PostMapping("/permission/remove")
    @AdminRequired
    @PermissionMeta(value = "删除多个权限", mount = false)
    public UnifyResponseVO<String> removePermissions(@RequestBody @Validated RemovePermissionListDTO validator) {
        adminService.removePermissions(validator);
        return ResponseUtil.generateUnifyResponse(8);
    }

}
