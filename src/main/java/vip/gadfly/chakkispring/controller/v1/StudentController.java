package vip.gadfly.chakkispring.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.annotation.StudentClassCheck;
import vip.gadfly.chakkispring.common.util.IPUtil;
import vip.gadfly.chakkispring.common.util.PageUtil;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.SignListVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import vip.gadfly.chakkispring.vo.WorkForStudentVO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

import static vip.gadfly.chakkispring.common.constant.ClassVerifyConstant.*;

/**
 * @author Gadfly
 */
@RestController
@RequestMapping("/v1/class")
@PermissionModule(value = "学生")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;

    @GroupRequired
    @PermissionMeta(value = "查看自己所属班级")
    @GetMapping("/list")
    public List<ClassDO> getClassList() {
        return studentService.getStudentClassList();
    }

    @GetMapping("/class/list")
    @GroupRequired
    @PermissionMeta(value = "查询学生本学期所属班级")
    public List<ClassDO> getClassesBySemesterAndStudent(@RequestParam("semester_id") Integer semesterId) {
        Integer userId = LocalUser.getLocalUser().getId();
        return classService.getClassesBySemesterAndStudent(semesterId, userId);
    }

    @GetMapping("/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询特定班级")
    @StudentClassCheck(valueType = "classId", paramType = "PathVariable")
    public ClassDO getClass(@PathVariable @Positive(message = "{id}") Integer id) {
        return classService.getClass(id);
    }

    // 停用
    @GroupRequired
    @PermissionMeta(value = "查看班级内签到项目")
    // @GetMapping("/sign/list")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO getSignList(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<SignListVO> iPage = classService.getSignPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @GroupRequired
    @PermissionMeta(value = "查看班级最新签到项目")
    @GetMapping("/sign/latest")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public SignListVO getLatestSign(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId) {
        return studentService.getLatestSignByClassId(classId);
    }

    @GroupRequired
    @PermissionMeta(value = "学生进行签到")
    @PostMapping("/sign/confirm/{signId}")
    @StudentClassCheck(valueType = signIdType, paramType = pathVariableType)
    public UnifyResponseVO confirmStudentSign(
            @Min(value = 1, message = "{lesson.sign.id.positive}")
            @PathVariable Integer signId,
            HttpServletRequest request) {
        if (!studentService.signAvailable(signId)) {
            return ResponseUtil.generateUnifyResponse(10211);
        }
        String ip = IPUtil.getIPFromRequest(request);
        if (!studentService.confirmSign(signId, ip)) {
            return ResponseUtil.generateUnifyResponse(10210);
        }
        return ResponseUtil.generateUnifyResponse(20);
    }

    @GroupRequired
    @GetMapping("/semester/all")
    @PermissionMeta(value = "学生查询所有学期")
    public List<SemesterDO> getAllSemesters() {
        return classService.getAllSemesters();
    }

    @GroupRequired
    @PermissionMeta(value = "查看班级内作业项目")
    @GetMapping("/work/list")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO getWorkList(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<WorkForStudentVO> iPage = classService.getWorkPageForStudentByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

}
