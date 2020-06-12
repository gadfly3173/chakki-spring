package vip.gadfly.chakkispring.controller.cms;

import io.github.talelin.core.annotation.AdminMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.admin.NewClassDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateClassDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;

import javax.validation.constraints.Positive;
import java.util.List;

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
    @AdminMeta(permission = "查询所有权限组", module = "管理员")
    public List<ClassDO> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/class/{id}")
    @AdminMeta(permission = "查询一个班级及其权限", module = "管理员")
    public ClassDO getClass(@PathVariable @Positive(message = "{id}") Long id) {
        return classService.getClass(id);
    }

    @PostMapping("/class")
    @AdminMeta(permission = "新建班级", module = "管理员")
    public UnifyResponseVO createClass(@RequestBody @Validated NewClassDTO validator) {
        classService.createClass(validator);
        return ResponseUtil.generateUnifyResponse(13);
    }

    @PutMapping("/class/{id}")
    @AdminMeta(permission = "更新一个班级", module = "管理员")
    public UnifyResponseVO updateClass(@PathVariable @Positive(message = "{id}") Long id,
                                       @RequestBody @Validated UpdateClassDTO validator) {
        classService.updateClass(id, validator);
        return ResponseUtil.generateUnifyResponse(5);
    }

    @DeleteMapping("/class/{id}")
    @AdminMeta(permission = "删除一个班级", module = "管理员")
    public UnifyResponseVO deleteClass(@PathVariable @Positive(message = "{id}") Long id) {
        classService.deleteClass(id);
        return ResponseUtil.generateUnifyResponse(6);
    }
}
