package vip.gadfly.chakkispring.common.interceptor;

import vip.gadfly.chakkispring.common.annotation.StudentClassCheck;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Gadfly
 *
 * 班级权限验证接口
 */
public interface ClassVerifyResolver {
    /**
     * 处理 当前的handler 不是 HandlerMethod 的情况
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否成功
     */
    boolean handleNotHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler);

    /**
     * 验证教师是否属于班级
     *
     * @param request           请求
     * @param response          响应
     * @param teacherClassCheck 注解
     * @return 是否成功
     */
    boolean handleTeacherClassCheck(HttpServletRequest request, HttpServletResponse response, TeacherClassCheck teacherClassCheck) throws IOException;

    /**
     * 验证学生是否属于班级
     *
     * @param request           请求
     * @param response          响应
     * @param studentClassCheck 注解
     * @return 是否成功
     */
    boolean handleStudentClassCheck(HttpServletRequest request, HttpServletResponse response, StudentClassCheck studentClassCheck) throws IOException;
}
