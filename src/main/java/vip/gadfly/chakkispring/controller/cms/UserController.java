package vip.gadfly.chakkispring.controller.cms;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.core.annotation.*;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.core.token.Tokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.user.*;
import vip.gadfly.chakkispring.model.GroupDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.GroupService;
import vip.gadfly.chakkispring.service.LogService;
import vip.gadfly.chakkispring.service.UserIdentityService;
import vip.gadfly.chakkispring.service.UserService;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import vip.gadfly.chakkispring.vo.UserInfoVO;
import vip.gadfly.chakkispring.vo.UserPermissionVO;

import java.util.List;
import java.util.Map;

/**
 * Created by lin on 2019/05/23.
 * License MIT
 */

@RestController
@RequestMapping("/cms/user")
@PermissionModule(value = "用户")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private DoubleJWT jwt;

    @Autowired
    private LogService logService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @AdminRequired
    public UnifyResponseVO<String> register(@RequestBody @Validated RegisterDTO validator) {
        userService.createUser(validator);
        return ResponseUtil.generateUnifyResponse(9);
    }

    /**
     * 用户批量注册
     */
    @PostMapping("/register/batch")
    @AdminRequired
    public UnifyResponseVO<String> register(@RequestBody @Validated BatchRegisterDTO validator) {
        userService.createBatchUser(validator);
        return ResponseUtil.generateUnifyResponse(9);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Tokens login(@RequestBody @Validated LoginDTO validator) {
        UserDO user = userService.getUserByUsername(validator.getUsername());
        if (user == null) {
            throw new NotFoundException(10021);
        }
        boolean valid = userIdentityService.verifyUsernamePassword(
                user.getId(),
                user.getUsername(),
                validator.getPassword());
        if (!valid) {
            throw new ParameterException(10031);
        }
        logService.createLog(
                user.getUsername() + "登录成功获取了令牌",
                "", user.getId(), user.getUsername(),
                "post",
                "/cms/user/login",
                200
        );
        return jwt.generateTokens(user.getId());
    }

    /**
     * 更新用户信息
     * <p>
     * 停用
     */
    @PutMapping
    @GroupRequired
    private UnifyResponseVO update(@RequestBody @Validated UpdateInfoDTO validator) {
        userService.updateUserInfo(validator);
        return ResponseUtil.generateUnifyResponse(4);
    }

    /**
     * 修改密码
     */
    @PutMapping("/change_password")
    @GroupRequired
    public UnifyResponseVO updatePassword(@RequestBody @Validated ChangePasswordDTO validator) {
        userService.changeUserPassword(validator);
        return ResponseUtil.generateUnifyResponse(2);
    }

    /**
     * 刷新令牌
     */
    @GetMapping("/refresh")
    @RefreshRequired
    public Tokens getRefreshToken() {
        UserDO user = LocalUser.getLocalUser();
        return jwt.generateTokens(user.getId());
    }

    /**
     * 查询拥有权限
     */
    @GetMapping("/permissions")
    @GroupRequired
    @PermissionMeta(value = "查询自己拥有的权限")
    public UserPermissionVO getPermissions() {
        UserDO user = LocalUser.getLocalUser();
        boolean admin = groupService.checkIsRootByUserId(user.getId());
        List<Map<String, List<Map<String, String>>>> permissions =
                userService.getStructuralUserPermissions(user.getId());
        UserPermissionVO userPermissions = new UserPermissionVO(user, permissions);
        userPermissions.setAdmin(admin);
        return userPermissions;
    }

    /**
     * 查询自己信息
     */
    @GetMapping("/information")
    @GroupRequired
    @PermissionMeta(value = "查询自己信息")
    public UserInfoVO getInformation() {
        UserDO user = LocalUser.getLocalUser();
        List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
        return new UserInfoVO(user, groups);
    }
}
