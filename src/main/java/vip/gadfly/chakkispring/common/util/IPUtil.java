package vip.gadfly.chakkispring.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;


/**
 * 班级用户关联查询工具
 */
@SuppressWarnings("unchecked")
@Slf4j
public class IPUtil {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getIPFromRequest(HttpServletRequest request) {
        String ip = null;
        if (request == null) {
            if (RequestContextHolder.getRequestAttributes() == null) {
                return null;
            }
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(ip))
            return ip;

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }

        return request.getRemoteAddr();
    }
}
