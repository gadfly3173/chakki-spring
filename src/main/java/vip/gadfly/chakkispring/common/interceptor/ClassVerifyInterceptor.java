package vip.gadfly.chakkispring.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import vip.gadfly.chakkispring.common.annotation.StudentClassCheck;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Gadfly
 */

/**
 * 班级访问拦截器
 */
@Component
public class ClassVerifyInterceptor implements HandlerInterceptor {

    @Autowired
    private ClassVerifyResolver classVerifyResolver;

    private String[] excludeMethods = new String[]{"OPTIONS"};

    public ClassVerifyInterceptor() {
    }

    /**
     * 构造函数
     *
     * @param excludeMethods 不检查方法
     */
    public ClassVerifyInterceptor(String[] excludeMethods) {
        this.excludeMethods = excludeMethods;
    }

    /**
     * 前置处理
     *
     * @param request  request 请求
     * @param response 相应
     * @param handler  处理器
     * @return 是否成功
     * @throws Exception 异常
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (this.checkInExclude(request.getMethod())) {
            // 有些请求方法无需检测，如OPTIONS
            return true;
        } else if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            TeacherClassCheck teacherClassCheck = method.getAnnotation(TeacherClassCheck.class);
            StudentClassCheck studentClassCheck = method.getAnnotation(StudentClassCheck.class);
            if (teacherClassCheck != null) {
                return classVerifyResolver.handleTeacherClassCheck(request, response, teacherClassCheck);
            }
            if (studentClassCheck != null) {
                return classVerifyResolver.handleStudentClassCheck(request, response, studentClassCheck);
            }
            return true;
        } else {
            // handler不是HandlerMethod的情况
            return classVerifyResolver.handleNotHandlerMethod(request, response, handler);
        }
    }

    private boolean checkInExclude(String method) {
        for (String excludeMethod : excludeMethods) {
            if (method.equals(excludeMethod)) {
                return true;
            }
        }
        return false;
    }
}
