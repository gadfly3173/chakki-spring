package vip.gadfly.chakkispring.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import io.github.talelin.autoconfigure.exception.AuthorizationException;
import org.springframework.stereotype.Component;
import vip.gadfly.chakkispring.common.annotation.StudentClassCheck;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;
import vip.gadfly.chakkispring.common.util.ClassPermissionCheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static vip.gadfly.chakkispring.common.constant.ClassVerifyConstant.*;

/**
 * @author Gadfly
 */

@SuppressWarnings("Duplicates")
@Component
public class ClassVerifyResolverImpl implements ClassVerifyResolver {
    @Override
    public boolean handleNotHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public boolean handleTeacherClassCheck(HttpServletRequest request, HttpServletResponse response, TeacherClassCheck teacherClassCheck) throws IOException {
        Long id;
        switch (teacherClassCheck.paramType()) {
            case pathVariableType:
                id = getIdByPathVariable(request);
                break;
            case requestParamType:
                id = getIdByRequestParam(request, teacherClassCheck.valueName());
                break;
            case requestBodyType:
                id = getIdByRequestBody(request, teacherClassCheck.valueName());
                break;
            default:
                throw new RuntimeException("id的参数类型错误(PathVariable, RequestParam)，请开发检查");
        }
        boolean result;
        switch (teacherClassCheck.valueType()) {
            case classIdType:
                result = ClassPermissionCheckUtil.isTeacherInClassByClassId(id);
                break;
            case signIdType:
                result = ClassPermissionCheckUtil.isTeacherInClassBySignId(id);
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
    public boolean handleStudentClassCheck(HttpServletRequest request, HttpServletResponse response, StudentClassCheck studentClassCheck) throws IOException {
        Long id;
        switch (studentClassCheck.paramType()) {
            case pathVariableType:
                id = getIdByPathVariable(request);
                break;
            case requestParamType:
                id = getIdByRequestParam(request, studentClassCheck.valueName());
                break;
            case requestBodyType:
                id = getIdByRequestBody(request, studentClassCheck.valueName());
                break;
            default:
                throw new RuntimeException("id的参数类型错误(PathVariable, RequestParam)，请开发检查");
        }
        boolean result;
        switch (studentClassCheck.valueType()) {
            case classIdType:
                result = ClassPermissionCheckUtil.isStudentInClassByClassId(id);
                break;
            case signIdType:
                result = ClassPermissionCheckUtil.isStudentInClassBySignId(id);
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

    private Long getIdByRequestBody(HttpServletRequest request, String valueName) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
        JSONObject tmpJson = jsonObject;
        String[] keyList = valueName.split("\\.");
        if (keyList.length > 1) {
            for (int x = 0; x < keyList.length - 1; x++) {
                tmpJson = jsonObject.getJSONObject(keyList[x]);
            }
        }
        return tmpJson.getLong(keyList[keyList.length - 1]);
    }
}