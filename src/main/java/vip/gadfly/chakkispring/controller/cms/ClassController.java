package vip.gadfly.chakkispring.controller.cms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.AdminMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.admin.NewClassDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateClassDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import vip.gadfly.chakkispring.vo.UserInfoVO;

import javax.validation.constraints.Min;
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
    @AdminMeta(permission = "查询所有班级", module = "管理员")
    public List<ClassDO> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/class/{id}")
    @AdminMeta(permission = "查询一个班级", module = "管理员")
    public ClassDO getClass(@PathVariable @Positive(message = "{id}") Long id) {
        return classService.getClass(id);
    }

    @PostMapping("/class")
    @AdminMeta(permission = "新建班级", module = "管理员")
    public UnifyResponseVO createClass(@RequestBody @Validated NewClassDTO validator) {
        classService.createClass(validator);
        return ResponseUtil.generateUnifyResponse(10207);
    }

    @PutMapping("/class/{id}")
    @AdminMeta(permission = "更新一个班级", module = "管理员")
    public UnifyResponseVO updateClass(@PathVariable @Positive(message = "{id}") Long id,
                                       @RequestBody @Validated UpdateClassDTO validator) {
        classService.updateClass(id, validator);
        return ResponseUtil.generateUnifyResponse(10205);
    }

    @DeleteMapping("/class/{id}")
    @AdminMeta(permission = "删除一个班级", module = "管理员")
    public UnifyResponseVO deleteClass(@PathVariable @Positive(message = "{id}") Long id) {
        classService.deleteClass(id);
        return ResponseUtil.generateUnifyResponse(10206);
    }

    @GetMapping("/students")
    @AdminMeta(permission = "查询所有学生", module = "管理员")
    public PageResponseVO getStudents(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long groupId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<UserDO> iPage = classService.getUserPageByClassId(groupId, count, page);
        List<UserInfoVO> userInfos = iPage.getRecords().stream().map(user -> {
            List<ClassDO> lesson = classService.getUserClassByUserId(user.getId());
            return new UserInfoVO(user, lesson);
        }).collect(Collectors.toList());
        return ResponseUtil.generatePageResult(iPage.getTotal(), userInfos, page, count);
    }
}
