package vip.gadfly.chakkispring.controller.cms;

import io.github.talelin.autoconfigure.exception.AuthorizationException;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.core.annotation.*;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.core.token.Tokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.common.util.ValidateCodeUtil;
import vip.gadfly.chakkispring.dto.user.*;
import vip.gadfly.chakkispring.model.GroupDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.*;
import vip.gadfly.chakkispring.vo.TokensWithMFA;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import vip.gadfly.chakkispring.vo.UserInfoVO;
import vip.gadfly.chakkispring.vo.UserPermissionVO;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
@Slf4j
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

    @Autowired
    private GoogleAuthenticatorService googleAuthenticatorService;

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
    public TokensWithMFA login(@RequestBody @Validated LoginDTO validator, HttpSession session) {
        String sessionCode = String.valueOf(session.getAttribute(ValidateCodeUtil.sessionKey));
        String receivedCode = validator.getCaptcha();
        if (!sessionCode.equalsIgnoreCase(receivedCode)) {
            throw new FailedException(10101);
        }
        UserDO user = userService.getUserByUsername(validator.getUsername());
        if (user == null) {
            throw new NotFoundException(10021);
        }
        long startTime = System.currentTimeMillis();
        boolean valid = userIdentityService.verifyUsernamePassword(
                user.getId(),
                user.getUsername(),
                validator.getPassword());
        log.info("verifyUsernamePassword cost : " + (System.currentTimeMillis() - startTime) + "ms");
        if (!valid) {
            throw new ParameterException(10031);
        }
        boolean MFARequire = googleAuthenticatorService.MFAexist(user.getId());
        if (MFARequire) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userid", user.getId());
            session.setMaxInactiveInterval(300);
            return TokensWithMFA.builder().MFARequire(true).build();
        }
        logService.createLog(
                user.getUsername() + "登录成功获取了令牌",
                "", user.getId(), user.getUsername(),
                "post",
                "/cms/user/login",
                200
        );
        Tokens tokens = jwt.generateTokens(user.getId());
        TokensWithMFA tokensWithMFA = new TokensWithMFA();
        BeanUtils.copyProperties(tokens, tokensWithMFA);
        return tokensWithMFA;
    }

    @PostMapping("/login_with_mfa/{code}")
    public Tokens loginWithMFA(@PathVariable @NotNull Integer code, HttpSession session) {
        String username = String.valueOf(session.getAttribute("username"));
        Integer userId = (Integer) session.getAttribute("userid");
        if (!StringUtils.hasText(username) || userId == null) {
            throw new FailedException(10102);
        }
        if (!googleAuthenticatorService.validCodeWithUsername(username, code)) {
            throw new FailedException(10102);
        }
        logService.createLog(
                username + "通过MFA登录成功获取了令牌",
                "", userId, username,
                "post",
                "/cms/user/login_with_mfa",
                200
        );
        return jwt.generateTokens(userId);
    }

    @GetMapping("/get_captcha_img")
    public String getCaptchaImg(HttpSession session) throws IOException {
        ValidateCodeUtil validateCode = new ValidateCodeUtil();
        // 返回base64
        String base64String = validateCode.getRandomCodeBase64(session);
        return "data:image/png;base64," + base64String;
    }

    /**
     * 更新用户信息
     * <p>
     * 停用
     */
    // @PutMapping
    @GroupRequired
    public UnifyResponseVO<String> update(@RequestBody @Validated UpdateInfoDTO validator) {
        userService.updateUserInfo(validator);
        return ResponseUtil.generateUnifyResponse(4);
    }

    /**
     * 获取两步验证密钥
     */
    @PostMapping("/get_mfa_secret")
    @GroupRequired
    public String setUserMFASecret(HttpSession session) throws UnsupportedEncodingException {
        UserDO user = LocalUser.getLocalUser();
        if (googleAuthenticatorService.MFAexist(user.getId())) {
            throw new FailedException(10103);
        }
        return googleAuthenticatorService.getQrUrl(user.getUsername(), user.getId(), session);
    }

    /**
     * 获取两步验证开通状态
     */
    @GetMapping("/mfa")
    @GroupRequired
    public boolean getUserMFAStatus() {
        Integer userId = LocalUser.getLocalUser().getId();
        return userService.getUserMFAStatus(userId);
    }

    /**
     * 删除两步验证密钥
     */
    @PostMapping("/delete_mfa_secret/{code}")
    @GroupRequired
    public UnifyResponseVO<String> deleteUserMFASecret(@PathVariable @NotNull Integer code) {
        UserDO user = LocalUser.getLocalUser();
        if (googleAuthenticatorService.validCodeWithUsername(user.getUsername(), code)) {
            googleAuthenticatorService.cancelMFA(user.getId());
            return ResponseUtil.generateUnifyResponse(34);
        }
        throw new AuthorizationException(10102);
    }

    /**
     * 启用后验证两步验证密钥，成功才会写入数据库
     */
    @PostMapping("/confirm_mfa_secret/{code}")
    @GroupRequired
    public UnifyResponseVO<String> confirmUserMFASecret(@PathVariable @NotNull Integer code, HttpSession session) {
        UserDO user = LocalUser.getLocalUser();
        String secretKey = String.valueOf(session.getAttribute("secretKey"));
        if (!StringUtils.hasText(secretKey)) {
            throw new FailedException(10104);
        }
        if (googleAuthenticatorService.validCodeWithSecret(secretKey, code)) {
            googleAuthenticatorService.saveUserCredentials(secretKey, user.getId());
            return ResponseUtil.generateUnifyResponse(35);
        }
        throw new FailedException(10104);
    }

    /**
     * 修改密码
     */
    @PutMapping("/change_password")
    @GroupRequired
    public UnifyResponseVO<String> updatePassword(@RequestBody @Validated ChangePasswordDTO validator) {
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
