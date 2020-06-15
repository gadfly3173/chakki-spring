package vip.gadfly.chakkispring.controller.cms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.AdminMeta;
import io.github.talelin.core.annotation.GroupMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.admin.AddStudentClassDTO;
import vip.gadfly.chakkispring.dto.admin.DispatchStudentClassDTO;
import vip.gadfly.chakkispring.dto.admin.NewClassDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateClassDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import vip.gadfly.chakkispring.vo.UserInfoVO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gadfly
 */

@RestController
@RequestMapping("/cms/admin")
@Validated
public class ClassController {

    @Autowired
    private ClassService classService;

//    班级接口

    @GetMapping("/class/all")
    @GroupMeta(permission = "查询所有班级", module = "管理员", mount = true)
    public List<ClassDO> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/class/{id}")
    @GroupMeta(permission = "查询一个班级", module = "管理员", mount = true)
    public ClassDO getClass(@PathVariable @Positive(message = "{id}") Long id) {
        return classService.getClass(id);
    }

    @PostMapping("/class")
    @GroupMeta(permission = "新建班级", module = "管理员", mount = true)
    public UnifyResponseVO createClass(@RequestBody @Validated NewClassDTO validator) {
        classService.createClass(validator);
        return ResponseUtil.generateUnifyResponse(16);
    }

    @PutMapping("/class/{id}")
    @GroupMeta(permission = "更新一个班级", module = "管理员", mount = true)
    public UnifyResponseVO updateClass(@PathVariable @Positive(message = "{id}") Long id,
                                       @RequestBody @Validated UpdateClassDTO validator) {
        classService.updateClass(id, validator);
        return ResponseUtil.generateUnifyResponse(14);
    }

    @DeleteMapping("/class/{id}")
    @GroupMeta(permission = "删除一个班级", module = "管理员", mount = true)
    public UnifyResponseVO deleteClass(@PathVariable @Positive(message = "{id}") Long id) {
        classService.deleteClass(id);
        return ResponseUtil.generateUnifyResponse(15);
    }

    @GetMapping("/students")
    @GroupMeta(permission = "查询所有此班级学生", module = "管理员", mount = true)
    public PageResponseVO getStudents(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<UserDO> iPage = classService.getUserPageByClassId(classId, count, page);
//        List<UserInfoVO> userInfos = iPage.getRecords().stream().map(user -> {
//            List<ClassDO> lesson = classService.getUserClassByUserId(user.getId());
//            return new UserInfoVO(user, lesson);
//        }).collect(Collectors.toList());
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @GetMapping("/students/fresh")
    @GroupMeta(permission = "查询所有不在此班级的学生", module = "管理员", mount = true)
    public PageResponseVO getFreshStudents(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<UserDO> iPage = classService.getFreshUserPageByClassId(classId, count, page);
//        List<UserInfoVO> userInfos = iPage.getRecords().stream().map(user -> {
//            List<ClassDO> lesson = classService.getUserClassByUserId(user.getId());
//            return new UserInfoVO(user, lesson);
//        }).collect(Collectors.toList());
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @GetMapping("/students/fresh_by_name")
    @GroupMeta(permission = "查询名字符合的不在此班级的学生", module = "管理员", mount = true)
    public PageResponseVO getFreshStudentsByName(
            @RequestParam(name = "name")
            @NotBlank(message = "{search-text.blank}") String name,
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<UserDO> iPage = classService.getFreshUserPageByClassIdAndName(classId, name, count, page);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @PostMapping("/students/del")
    @GroupMeta(permission = "移除班级内学生", module = "管理员", mount = true)
    public UnifyResponseVO moveStudentClass(@RequestBody @Validated DispatchStudentClassDTO validator) {
        classService.deleteStudentClassRelations(validator.getUserId(), validator.getClassIds());
        return ResponseUtil.generateUnifyResponse(17);
    }

    @PostMapping("/students/add")
    @GroupMeta(permission = "添加班级内学生", module = "管理员", mount = true)
    public UnifyResponseVO addStudentClass(@RequestBody @Validated AddStudentClassDTO validator) {
        classService.addStudentClassRelations(validator.getClassId(), validator.getUserIds());
        return ResponseUtil.generateUnifyResponse(18);
    }
}
