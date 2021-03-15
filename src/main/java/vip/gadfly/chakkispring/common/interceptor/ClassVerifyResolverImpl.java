package vip.gadfly.chakkispring.common.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.talelin.autoconfigure.exception.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class ClassVerifyResolverImpl implements ClassVerifyResolver {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public boolean handleNotHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public boolean handleTeacherClassCheck(HttpServletRequest request, HttpServletResponse response, TeacherClassCheck teacherClassCheck) throws IOException {
        Integer id;
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
                throw new RuntimeException("id的参数类型错误，请开发检查");
        }
        if (id == 0) {
            throw new RuntimeException("id的参数类型错误，请开发检查");
        }
        boolean result;
        switch (teacherClassCheck.valueType()) {
            case classIdType:
                result = ClassPermissionCheckUtil.isTeacherInClassByClassId(id);
                break;
            case signIdType:
                result = ClassPermissionCheckUtil.isTeacherInClassBySignId(id);
                break;
            case workIdType:
                result = ClassPermissionCheckUtil.isTeacherInClassByWorkId(id);
                break;
            case studentWorkIdType:
                result = ClassPermissionCheckUtil.isTeacherInClassByStudentWorkId(id);
                break;
            case announcementIdType:
                result = ClassPermissionCheckUtil.isTeacherInClassByAnnouncementId(id);
                break;
            default:
                throw new RuntimeException("id的值类型错误，请开发检查");
        }
        if (!result) {
            throw new AuthorizationException(10002);
        }
        log.info(String.format("TeacherClassCheckOK, id:%s, type:%s, paramType:%s", id, teacherClassCheck.valueType(), teacherClassCheck.paramType()));
        return true;
    }

    @Override
    public boolean handleStudentClassCheck(HttpServletRequest request, HttpServletResponse response, StudentClassCheck studentClassCheck) throws IOException {
        Integer id;
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
                throw new RuntimeException("id的参数类型错误，请开发检查");
        }
        if (id == 0) {
            throw new RuntimeException("id的参数类型错误，请开发检查");
        }
        boolean result;
        switch (studentClassCheck.valueType()) {
            case classIdType:
                result = ClassPermissionCheckUtil.isStudentInClassByClassId(id);
                break;
            case signIdType:
                result = ClassPermissionCheckUtil.isStudentInClassBySignId(id);
                break;
            case workIdType:
                result = ClassPermissionCheckUtil.isStudentInClassByWorkId(id);
                break;
            case studentWorkIdType:
                result = ClassPermissionCheckUtil.isStudentInClassByStudentWorkId(id);
                break;
            case announcementIdType:
                result = ClassPermissionCheckUtil.isStudentInClassByAnnouncementId(id);
                break;
            default:
                throw new RuntimeException("id的值类型错误，请开发检查");
        }
        if (!result) {
            throw new AuthorizationException(10002);
        }
        log.info(String.format("StudentClassCheckOK, id:%s, type:%s, paramType:%s", id, studentClassCheck.valueType(), studentClassCheck.paramType()));
        return true;
    }

    private Integer getIdByRequestParam(HttpServletRequest request, String valueName) {
        return Integer.valueOf(request.getParameter(valueName));
    }

    private Integer getIdByPathVariable(HttpServletRequest request) {
        String path = request.getRequestURI();
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        return Integer.valueOf(idStr);
    }

    private Integer getIdByRequestBody(HttpServletRequest request, String valueName) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        JsonNode jsonObject = mapper.readTree(responseStrBuilder.toString());
        JsonNode tmpJson = jsonObject;
        String[] keyList = valueName.split("\\.");
        if (keyList.length > 1) {
            for (int x = 0; x < keyList.length - 1; x++) {
                tmpJson = jsonObject.get(keyList[x]);
            }
        }
        if (tmpJson.get(keyList[keyList.length - 1]) == null) {
            return 0;
        }
        return tmpJson.get(keyList[keyList.length - 1]).asInt();
    }
}
