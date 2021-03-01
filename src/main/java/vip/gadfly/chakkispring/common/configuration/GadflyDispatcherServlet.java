package vip.gadfly.chakkispring.common.configuration;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Gadfly
 * @summary 自定义 DispatcherServlet 来分派 GadflyHttpServletRequestWrapper
 */
public class GadflyDispatcherServlet extends DispatcherServlet {

    /**
     * 包装成我们自定义的request
     *
     * @param request request
     * @param response response
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest;
        boolean multipartRequestParsed;
        processedRequest = checkMultipart(request);
        multipartRequestParsed = (processedRequest != request);
        // Clean up any resources used by a multipart request.
        if (multipartRequestParsed) {
            super.doDispatch(request, response);
            return;
        }
        super.doDispatch(new GadflyHttpServletRequestWrapper(request), response);
    }
}
