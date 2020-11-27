package vip.gadfly.chakkispring.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.GroupMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.admin.AddStudentClassDTO;
import vip.gadfly.chakkispring.dto.admin.DispatchStudentClassDTO;
import vip.gadfly.chakkispring.dto.admin.NewClassDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateClassDTO;
import vip.gadfly.chakkispring.dto.lesson.NewSignDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SignListDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.StudentSignVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Gadfly
 */

@RestController
@RequestMapping("/v1/lesson")
@Validated
public class LessonController {

    @Autowired
    private ClassService classService;

//    班级接口

    @GetMapping("/class/all")
    @GroupMeta(permission = "查询所有班级", module = "教师", mount = true)
    public List<ClassDO> getAllClasses() {
        return classService.getAllClasses();
    }

   @GetMapping("/class/{id}")
   @GroupMeta(permission = "查询一个班级", module = "教师", mount = true)
   public ClassDO getClass(@PathVariable @Positive(message = "{id}") Long id) {
       return classService.getClass(id);
   }

    @GetMapping("/students")
    @GroupMeta(permission = "查询所有此班级学生", module = "教师", mount = true)
    public PageResponseVO getStudents(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<UserDO> iPage = classService.getUserPageByClassId(classId, count, page);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @PostMapping("/sign/create")
    @GroupMeta(permission = "发起签到", module = "教师", mount = true)
    public UnifyResponseVO createStudentSign(@RequestBody @Validated NewSignDTO validator) {
        classService.createSign(validator);
        return ResponseUtil.generateUnifyResponse(19);
    }

    @GetMapping("/sign/list")
    @GroupMeta(permission = "查看所有签到项目", module = "教师", mount = true)
    public PageResponseVO getSignList(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<SignListDO> iPage = classService.getSignPageByClassId(classId, count, page);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @GetMapping("/sign/students/query/{signId}")
    @GroupMeta(permission = "查询所有签到项目下的学生", module = "教师", mount = true)
    public PageResponseVO getStudentsBySignId(
            @Min(value = 1, message = "{sign-status}")
            @RequestParam(name = "sign_status", required = false, defaultValue = "0") Integer signStatus,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page,
            @Min(value = 1, message = "{lesson.sign.id.positive}")
            @PathVariable Long signId) {
        IPage<StudentSignVO> iPage = classService.getUserPageBySignId(signId, signStatus, count, page);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }
}
