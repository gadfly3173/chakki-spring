package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.ICredentialRepository;
import io.github.talelin.autoconfigure.exception.FailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gadfly.chakkispring.mapper.UserMFAMapper;
import vip.gadfly.chakkispring.mapper.UserMapper;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.model.UserMFADO;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Gadfly
 */
@Slf4j
@Service
public class GoogleAuthenticatorService {

    private static final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    private static final String KEY_FORMAT = "otpauth://totp/%s:%s?secret=%s&issuer=%s";

    @Autowired
    private UserMFAMapper userMFAMapper;

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void init() {
        googleAuthenticator.setCredentialRepository(new ICredentialRepository() {
            @Override
            public String getSecretKey(String username) {
                QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(UserDO::getUsername, username);
                UserDO userDO = userMapper.selectOne(wrapper);
                return userMFAMapper.getSecretKeyByUserId(userDO.getId());
            }

            @Override
            public void saveUserCredentials(String username, String secretKey, int validationCode, List<Integer> scratchCodes) {
                // secretKey要保存在数据库中
                QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(UserDO::getUsername, username);
                UserDO userDO = userMapper.selectOne(wrapper);
                UserMFADO userMFADO = UserMFADO.builder().userId(userDO.getId()).secret(secretKey).build();
                userMFAMapper.insert(userMFADO);
            }
        });
        log.info("GoogleAuthenticator初始化成功");
    }

    /**
     * 生成二维码链接
     */
    public String getQrUrl(String username, Integer userId) throws UnsupportedEncodingException {
        QueryWrapper<UserMFADO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserMFADO::getUserId, userId);
        if (userMFAMapper.selectCount(wrapper) > 0) {
            throw new FailedException(10103);
        }
        //每次调用createCredentials都会生成新的secretKey
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials(username);
        log.info("username={},secretKey={}", username, key.getKey());
        return String.format(KEY_FORMAT, "Chakki", URLEncoder.encode(username, StandardCharsets.UTF_8.name()), key.getKey(), "Chakki");
    }

    public boolean validCode(String username, int code) {
        return googleAuthenticator.authorizeUser(username, code);
    }

    public boolean notNewCreated(Integer userId) {
        QueryWrapper<UserMFADO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserMFADO::getUserId, userId);
        long createTimestamp = userMFAMapper.selectOne(wrapper).getCreateTime().getTime();
        return System.currentTimeMillis() - createTimestamp > 1000 * 60 * 5;
    }

    public void cancelMFA(Integer userId) {
        QueryWrapper<UserMFADO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserMFADO::getUserId, userId);
        userMFAMapper.delete(wrapper);
    }

    public boolean MFAexist(Integer userId) {
        QueryWrapper<UserMFADO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserMFADO::getUserId, userId);
        return userMFAMapper.selectCount(wrapper) > 0;
    }
}
