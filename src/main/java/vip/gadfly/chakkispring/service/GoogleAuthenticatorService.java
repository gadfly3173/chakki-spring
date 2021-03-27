package vip.gadfly.chakkispring.service;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * @author Gadfly
 */

public interface GoogleAuthenticatorService {
    String getQrUrl(String username, Integer userId, HttpSession session) throws UnsupportedEncodingException;

    boolean validCodeWithUsername(String username, int code);

    boolean validCodeWithSecret(String secret, int code);

    void cancelMFA(Integer userId);

    boolean MFAexist(Integer userId);

    void saveUserCredentials(String secretKey, Integer userId, String username);
}
