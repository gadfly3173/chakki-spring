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
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.model.SignListDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.SignListVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;

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
    @PermissionMeta(value = "查看自己所属班级", mount = true)
    @GetMapping("/list")
    public List<ClassDO> getClassList() {
        return studentService.getStudentClassList();
    }

    @GetMapping("/class/list")
    @GroupRequired
    @PermissionMeta(value = "查询学生本学期所属班级", mount = true)
    public List<ClassDO> getClassesBySemesterAndStudent(@RequestParam("semester_id") Long semesterId) {
        Long userId = LocalUser.getLocalUser().getId();
        return classService.getClassesBySemesterAndStudent(semesterId, userId);
    }

    @GetMapping("/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询特定班级", mount = true)
    @StudentClassCheck(valueType = "classId", paramType = "PathVariable")
    public ClassDO getClass(@PathVariable @Positive(message = "{id}") Long id) {
        return classService.getClass(id);
    }

    @GroupRequired
    @PermissionMeta(value = "查看班级内签到项目", mount = true)
    @GetMapping("/sign/list")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
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

    @GroupRequired
    @PermissionMeta(value = "查看班级最新签到项目", mount = true)
    @GetMapping("/sign/latest")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public SignListVO getLatestSign(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId) {
        return studentService.getLatestSignByClassId(classId);
    }

    @GroupRequired
    @PermissionMeta(value = "学生进行签到", mount = true)
    @PostMapping("/sign/confirm/{signId}")
    @StudentClassCheck(valueType = signIdType, paramType = pathVariableType)
    public UnifyResponseVO confirmStudentSign(
            @Min(value = 1, message = "{lesson.sign.id.positive}")
            @PathVariable Long signId,
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
    @PermissionMeta(value = "学生查询所有学期", mount = true)
    public List<SemesterDO> getAllSemesters() {
        return classService.getAllSemesters();
    }
}
