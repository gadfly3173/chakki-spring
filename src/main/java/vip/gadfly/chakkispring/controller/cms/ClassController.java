package vip.gadfly.chakkispring.controller.cms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.util.PageUtil;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.admin.*;
import vip.gadfly.chakkispring.dto.query.ClassIdNamePageDTO;
import vip.gadfly.chakkispring.dto.query.ClassIdPageDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.TeacherClassVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Gadfly
 */

@Api(value = "/cms/admin", tags = "教务管理")
@RestController
@RequestMapping("/cms/admin")
@PermissionModule(value = "管理员")
@Validated
public class ClassController {

    @Autowired
    private ClassService classService;

    // 班级接口

    @GetMapping("/class/all")
    @GroupRequired
    @PermissionMeta(value = "查询所有班级")
    public List<ClassDO> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/class/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个班级")
    public ClassDO getClass(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getClass(id);
    }

    @PostMapping("/class")
    @GroupRequired
    @PermissionMeta(value = "新建班级")
    public UnifyResponseVO<String> createClass(@RequestBody @Validated NewClassDTO validator) {
        if (classService.createClass(validator)) {
            return ResponseUtil.generateUnifyResponse(16);
        }
        throw new FailedException(10200);
    }

    @PutMapping("/class/{id}")
    @GroupRequired
    @PermissionMeta(value = "更新一个班级")
    public UnifyResponseVO<String> updateClass(@PathVariable @Positive(message = "{id.positive}") Integer id,
                                               @RequestBody @Validated UpdateClassDTO validator) {
        if (classService.updateClass(id, validator)) {
            return ResponseUtil.generateUnifyResponse(14);
        }
        throw new FailedException(10200);
    }

    @DeleteMapping("/class/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除一个班级")
    public UnifyResponseVO<String> deleteClass(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        if (classService.deleteClass(id)) {
            return ResponseUtil.generateUnifyResponse(15);
        }
        throw new FailedException(10200);
    }

    @GetMapping("/students")
    @GroupRequired
    @PermissionMeta(value = "查询所有此班级学生")
    public PageResponseVO<UserDO> getStudents(@Validated ClassIdPageDTO classIdPageDTO) {
        IPage<UserDO> iPage = classService.getUserPageByClassId(classIdPageDTO.getClassId(),
                classIdPageDTO.getCount(),
                classIdPageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @GetMapping("/students/fresh")
    @GroupRequired
    @PermissionMeta(value = "查询所有不在此班级的学生")
    public PageResponseVO<UserDO> getFreshStudents(@Validated ClassIdPageDTO classIdPageDTO) {
        IPage<UserDO> iPage = classService.getFreshUserPageByClassId(classIdPageDTO.getClassId(),
                classIdPageDTO.getCount(),
                classIdPageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @GetMapping("/students/fresh_by_name")
    @GroupRequired
    @PermissionMeta(value = "查询名字符合的不在此班级的学生")
    public PageResponseVO<UserDO> getFreshStudentsByName(@Validated ClassIdNamePageDTO classIdNamePageDTO) {
        IPage<UserDO> iPage = classService.getFreshUserPageByClassIdAndName(
                classIdNamePageDTO.getClassId(),
                classIdNamePageDTO.getName(),
                classIdNamePageDTO.getCount(),
                classIdNamePageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @PostMapping("/students/del")
    @GroupRequired
    @PermissionMeta(value = "移除班级内学生")
    public UnifyResponseVO<String> removeStudentClass(@RequestBody @Validated DispatchStudentClassDTO validator) {
        if (classService.deleteStudentClassRelations(validator.getUserId(), validator.getClassIds())) {
            return ResponseUtil.generateUnifyResponse(17);
        }
        throw new FailedException(10206);
    }

    @PostMapping("/students/add")
    @GroupRequired
    @PermissionMeta(value = "添加班级内学生")
    public UnifyResponseVO<String> addStudentClass(@RequestBody @Validated AddStudentClassDTO validator) {
        if (classService.addStudentClassRelations(validator.getClassId(), validator.getUserIds())) {
            return ResponseUtil.generateUnifyResponse(18);
        }
        throw new FailedException(10207);
    }

    @GetMapping("/teacher/list")
    @GroupRequired
    @PermissionMeta(value = "查询班级内教师")
    public PageResponseVO<TeacherClassVO> getClassTeachers(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId) {
        IPage<TeacherClassVO> iPage = classService.getTeacherPageByClassId(classId);
        return PageUtil.build(iPage);
    }

    @GetMapping("/teacher/fresh_by_name")
    @GroupRequired
    @PermissionMeta(value = "查询不在班级内的教师")
    public PageResponseVO<UserDO> getFreshTeachersByName(@Validated ClassIdNamePageDTO classIdNamePageDTO) {
        IPage<UserDO> iPage = classService.getFreshTeacherPageByClassIdAndName(
                classIdNamePageDTO.getClassId(),
                classIdNamePageDTO.getName(),
                classIdNamePageDTO.getCount(),
                classIdNamePageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @PostMapping("/teacher/del")
    @GroupRequired
    @PermissionMeta(value = "移除班级内教师")
    public UnifyResponseVO<String> removeTeacherClass(@RequestBody @Validated DispatchTeacherClassDTO validator) {
        if (classService.deleteTeacherClassRelations(validator.getUserId(), validator.getClassIds())) {
            return ResponseUtil.generateUnifyResponse(22);
        }
        throw new FailedException(10208);
    }

    @PostMapping("/teacher/add")
    @GroupRequired
    @PermissionMeta(value = "添加班级内教师")
    public UnifyResponseVO<String> addTeacherClass(@RequestBody @Validated AddTeacherClassDTO validator) {
        if (classService.addTeacherClassRelations(validator.getClassId(), validator.getUserIds(),
                validator.getLevel())) {
            return ResponseUtil.generateUnifyResponse(23);
        }
        throw new FailedException(10209);
    }

    @PostMapping("/semester")
    @GroupRequired
    @PermissionMeta(value = "新建学期")
    public UnifyResponseVO<String> createSemester(@RequestBody @Validated NewSemesterDTO validator) {
        if (classService.createSemester(validator)) {
            return ResponseUtil.generateUnifyResponse(26);
        }
        throw new FailedException(10200);
    }

    @GetMapping("/semester/all")
    @GroupRequired
    @PermissionMeta(value = "查询所有学期")
    public List<SemesterDO> getAllSemesters() {
        return classService.getAllSemesters();
    }

    @PutMapping("/semester/{id}")
    @GroupRequired
    @PermissionMeta(value = "更新一个学期")
    public UnifyResponseVO<String> updateSemester(@PathVariable @Positive(message = "{id.positive}") Integer id,
                                                  @RequestBody @Validated UpdateSemesterDTO validator) {
        if (classService.updateSemester(id, validator)) {
            return ResponseUtil.generateUnifyResponse(24);
        }
        throw new FailedException(10200);
    }

    @DeleteMapping("/semester/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除一个学期")
    public UnifyResponseVO<String> deleteSemester(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        if (classService.deleteSemester(id)) {
            return ResponseUtil.generateUnifyResponse(25);
        }
        throw new FailedException(10200);
    }
}
