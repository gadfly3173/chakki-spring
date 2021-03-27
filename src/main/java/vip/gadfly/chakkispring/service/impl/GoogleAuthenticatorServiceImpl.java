package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.ICredentialRepository;
import io.github.talelin.autoconfigure.exception.FailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gadfly.chakkispring.common.constant.IdentityConstant;
import vip.gadfly.chakkispring.mapper.UserMapper;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.model.UserIdentityDO;
import vip.gadfly.chakkispring.service.GoogleAuthenticatorService;
import vip.gadfly.chakkispring.service.UserIdentityService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Gadfly
 */
@Slf4j
@Service
public class GoogleAuthenticatorServiceImpl implements GoogleAuthenticatorService {

    private static final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    private static final String KEY_FORMAT = "otpauth://totp/%s:%s?secret=%s&issuer=%s";

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void init() {
        googleAuthenticator.setCredentialRepository(new ICredentialRepository() {
            @Override
            public String getSecretKey(String username) {
                QueryWrapper<UserIdentityDO> identityDOQueryWrapper = new QueryWrapper<>();
                identityDOQueryWrapper.lambda()
                        .eq(UserIdentityDO::getIdentifier, username)
                        .eq(UserIdentityDO::getIdentityType, IdentityConstant.USERNAME_TOTP_IDENTITY);
                return userIdentityService.getBaseMapper().selectOne(identityDOQueryWrapper).getCredential();
            }

            @Override
            public void saveUserCredentials(String username, String secretKey, int validationCode, List<Integer> scratchCodes) {
                // secretKey要保存在数据库中
                QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(UserDO::getUsername, username);
                UserDO userDO = userMapper.selectOne(wrapper);
                userIdentityService.createIdentity(userDO.getId(), IdentityConstant.USERNAME_TOTP_IDENTITY, username, secretKey);
            }
        });
        log.info("GoogleAuthenticator 初始化成功");
    }

    /**
     * 生成二维码链接
     */
    @Override
    public String getQrUrl(String username, Integer userId, HttpSession session) throws UnsupportedEncodingException {
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserIdentityDO::getUserId, userId)
                .eq(UserIdentityDO::getIdentityType, IdentityConstant.USERNAME_TOTP_IDENTITY);
        if (userIdentityService.getBaseMapper().selectCount(wrapper) > 0) {
            throw new FailedException(10103);
        }
        // 每次调用createCredentials都会生成新的secretKey
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        log.info("username={}, secretKey={}", username, key.getKey());
        session.setAttribute("secretKey", key.getKey());
        session.setMaxInactiveInterval(300);
        return String.format(KEY_FORMAT,
                "Chakki",
                URLEncoder.encode(username, StandardCharsets.UTF_8.name()),
                key.getKey(),
                "Chakki");
    }

    @Override
    public boolean validCodeWithUsername(String username, int code) {
        return googleAuthenticator.authorizeUser(username, code);
    }

    @Override
    public boolean validCodeWithSecret(String secret, int code) {
        return googleAuthenticator.authorize(secret, code);
    }

    @Override
    public void cancelMFA(Integer userId) {
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserIdentityDO::getUserId, userId)
                .eq(UserIdentityDO::getIdentityType, IdentityConstant.USERNAME_TOTP_IDENTITY);
        userIdentityService.getBaseMapper().delete(wrapper);
    }

    @Override
    public boolean MFAexist(Integer userId) {
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserIdentityDO::getUserId, userId)
                .eq(UserIdentityDO::getIdentityType, IdentityConstant.USERNAME_TOTP_IDENTITY);
        return userIdentityService.getBaseMapper().selectCount(wrapper) > 0;
    }

    @Override
    public void saveUserCredentials(String secretKey, Integer userId, String username) {
        // secretKey要保存在数据库中
        userIdentityService.createIdentity(userId, IdentityConstant.USERNAME_TOTP_IDENTITY, username, secretKey);
    }
}
