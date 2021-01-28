package vip.gadfly.chakkispring.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.lesson.NewSignDTO;
import vip.gadfly.chakkispring.dto.lesson.UpdateSignRecordDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.model.SignListDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.SignCountVO;
import vip.gadfly.chakkispring.vo.StudentSignVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

import static vip.gadfly.chakkispring.common.constant.ClassVerifyConstant.*;

/**
 * @author Gadfly
 */

@RestController
@RequestMapping("/v1/lesson")
@PermissionModule(value = "教师")
@Validated
public class LessonController {

    @Autowired
    private ClassService classService;

    // 班级接口

    // 本接口暂时作废
    @GetMapping("/class/all")
    @GroupRequired
    @PermissionMeta(value = "查询所有班级", mount = true)
    private List<ClassDO> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/class/list")
    @GroupRequired
    @PermissionMeta(value = "查询教师本学期所属班级", mount = true)
    public List<ClassDO> getClassesBySemesterAndTeacher(@RequestParam("semester_id") Long semesterId) {
        Long teacherId = LocalUser.getLocalUser().getId();
        return classService.getClassesBySemesterAndTeacher(semesterId, teacherId);
    }

    @GetMapping("/class/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个班级", mount = true)
    @TeacherClassCheck(valueType = classIdType, paramType = pathVariableType)
    public ClassDO getClass(@PathVariable @Positive(message = "{id}") Long id) {
        return classService.getClass(id);
    }

    @GetMapping("/students")
    @GroupRequired
    @PermissionMeta(value = "查询所有此班级学生", mount = true)
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO getStudents(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<UserDO> iPage = classService.getUserPageByClassId(classId, count, page);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @PostMapping("/sign/create")
    @GroupRequired
    @PermissionMeta(value = "发起签到", mount = true)
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public UnifyResponseVO createStudentSign(@RequestBody @Validated NewSignDTO validator) {
        classService.createSign(validator);
        return ResponseUtil.generateUnifyResponse(19);
    }

    @GetMapping("/sign/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有签到项目", mount = true)
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO getSignList(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<SignListDO> iPage = classService.getSignPageByClassId(classId, count, page);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @GetMapping("/sign/students/query/{signId}")
    @GroupRequired
    @PermissionMeta(value = "查询单个签到项目下的所有学生", mount = true)
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public PageResponseVO getStudentsBySignId(
            @RequestParam(name = "sign_status", required = false, defaultValue = "0")
            @Min(value = 0, message = "{sign-status}") Integer signStatus,
            @RequestParam(name = "order_by_IP", required = false, defaultValue = "false")
                    boolean orderByIP,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page,
            @RequestParam(name = "username", required = false, defaultValue = "")
                    String username,
            @Min(value = 1, message = "{lesson.sign.id.positive}")
            @PathVariable Long signId) {
        IPage<StudentSignVO> iPage = classService.getUserPageBySignId(signId, signStatus, username, count, page,
                orderByIP);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @GetMapping("/sign/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个签到信息", mount = true)
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public SignCountVO getSign(@PathVariable @Positive(message = "{id}") Long id) {
        return classService.getSign(id);
    }

    @PostMapping("/sign/record/update/{signId}")
    @GroupRequired
    @PermissionMeta(value = "修改签到记录", mount = true)
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public UnifyResponseVO updateStudentSignRecord(@RequestBody @Validated UpdateSignRecordDTO validator,
                                                   @PathVariable Long signId) {
        if (classService.updateSignRecord(validator, signId)) {
            return ResponseUtil.generateUnifyResponse(21);
        }
        return ResponseUtil.generateUnifyResponse(10212);
    }

    @GetMapping("/semester/all")
    @GroupRequired
    @PermissionMeta(value = "教师查询所有学期", mount = true)
    public List<SemesterDO> getAllSemesters() {
        return classService.getAllSemesters();
    }
}
