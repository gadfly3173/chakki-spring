package vip.gadfly.chakkispring.extension.limit;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Gadfly
 */

public interface Limiter {

    boolean handle(HttpServletRequest request);
}
