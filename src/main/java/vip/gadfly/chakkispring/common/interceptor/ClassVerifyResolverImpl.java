package vip.gadfly.chakkispring.common.interceptor;

import io.github.talelin.autoconfigure.exception.AuthorizationException;
import org.springframework.stereotype.Component;
import vip.gadfly.chakkispring.common.annotation.StudentClassCheck;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;
import vip.gadfly.chakkispring.common.util.ClassPermissionCheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Gadfly
 */

@SuppressWarnings("Duplicates")
@Component
public class ClassVerifyResolverImpl implements ClassVerifyResolver{

    private final String pathVariableType = "PathVariable";
    private final String requestParamType = "RequestParam";

    private final String signIdType = "signId";
    private final String classIdType = "classId";

    @Override
    public boolean handleNotHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public boolean handleTeacherClassCheck(HttpServletRequest request, HttpServletResponse response, TeacherClassCheck teacherClassCheck) {
        Long id;
        switch (teacherClassCheck.paramType()) {
            case pathVariableType:
                id = getIdByPathVariable(request);
                break;
            case requestParamType:
                id = getIdByRequestParam(request, teacherClassCheck.valueName());
                break;
            default:
                throw new RuntimeException("id的参数类型错误(PathVariable, RequestParam)，请开发检查");
        }
        boolean result;
        switch (teacherClassCheck.valueType()) {
            case classIdType:
                result =  ClassPermissionCheckUtil.isTeacherInClassByClassId(id);
                break;
            case signIdType:
                result =  ClassPermissionCheckUtil.isTeacherInClassBySignId(id);
                break;
            default:
                throw new RuntimeException("id的值类型错误(classId, signId)，请开发检查");
        }
        if (!result) {
            throw new AuthorizationException(10002);
        }
        return true;
    }

    @Override
    public boolean handleStudentClassCheck(HttpServletRequest request, HttpServletResponse response, StudentClassCheck studentClassCheck) {
        Long id;
        switch (studentClassCheck.paramType()) {
            case pathVariableType:
                id = getIdByPathVariable(request);
                break;
            case requestParamType:
                id = getIdByRequestParam(request, studentClassCheck.valueName());
                break;
            default:
                throw new RuntimeException("id的参数类型错误(PathVariable, RequestParam)，请开发检查");
        }
        boolean result;
        switch (studentClassCheck.valueType()) {
            case classIdType:
                result =  ClassPermissionCheckUtil.isStudentInClassByClassId(id);
                break;
            case signIdType:
                result =  ClassPermissionCheckUtil.isStudentInClassBySignId(id);
                break;
            default:
                throw new RuntimeException("id的值类型错误(classId, signId)，请开发检查");
        }
        if (!result) {
            throw new AuthorizationException(10002);
        }
        return true;
    }

    private Long getIdByRequestParam(HttpServletRequest request, String valueName) {
        return Long.valueOf(request.getParameter(valueName));
    }

    private Long getIdByPathVariable(HttpServletRequest request) {
        String path = request.getRequestURI();
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        return Long.valueOf(idStr);
    }
}
