package vip.gadfly.chakkispring.extension.limit;

import io.github.talelin.autoconfigure.exception.RequestLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Gadfly
 */

@Component
@Slf4j
public class LimitInterceptor implements AsyncHandlerInterceptor {

    @Autowired
    private Limiter limiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean ok = limiter.handle(request);
        log.info("limit val : {}", ok);
        if (!ok) {
            throw new RequestLimitException(10140);
        }
        return true;
    }
}
