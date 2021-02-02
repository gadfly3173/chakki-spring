package vip.gadfly.chakkispring.extension.limit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gadfly
 */

@SuppressWarnings("UnstableApiUsage")
@Component
@Slf4j
public class MemoryLimiter implements Limiter {

    private final Map<String, RateLimiter> record = new ConcurrentHashMap<>();

    @Value("${lin.cms.limit.value}")
    private Integer value;

    @Override
    public boolean handle(HttpServletRequest request) {
        String uniqueId = getUniqueId(request);
        log.info("uniqueId: {}", uniqueId);
        RateLimiter currentLimiter = record.get(uniqueId);
        if (currentLimiter != null) {
            return currentLimiter.tryAcquire(1);
        } else {
            // 减去当前访问的一次
            RateLimiter limiter = RateLimiter.create(value);
            record.put(uniqueId, limiter);
            return true;
        }
    }

    private String getUniqueId(HttpServletRequest request) {
        return request.getLocalAddr();
    }
}
