package vip.gadfly.chakkispring.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.GroupMeta;
import io.github.talelin.core.annotation.LoginRequired;
import vip.gadfly.chakkispring.common.util.ClassPermissionCheckUtil;
import vip.gadfly.chakkispring.common.util.IPUtil;
import vip.gadfly.chakkispring.dto.lesson.NewSignDTO;
import vip.gadfly.chakkispring.model.BookDO;
import vip.gadfly.chakkispring.dto.book.CreateOrUpdateBookDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SignListDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.PageResponseVO;
import vip.gadfly.chakkispring.vo.SignListVO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Gadfly
 */
@RestController
@RequestMapping("/v1/class")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ClassService classService;

    @GroupMeta(permission = "查看自己所属班级", module = "学生", mount = true)
    @GetMapping("/list")
    public List<ClassDO> getClassList() {
        return studentService.getStudentClassList();
    }

    @GetMapping("/{id}")
    @GroupMeta(permission = "查询特定班级", module = "学生", mount = true)
    public ClassDO getClass(@PathVariable @Positive(message = "{id}") Long id) {
        if (!ClassPermissionCheckUtil.isStudentInClassByClassId(id)) {
            return null;
        }
        return classService.getClass(id);
    }

    @GroupMeta(permission = "查看班级内签到项目", module = "学生", mount = true)
    @GetMapping("/sign/list")
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

    @GroupMeta(permission = "查看班级最新签到项目", module = "学生", mount = true)
    @GetMapping("/sign/latest")
    public SignListVO getLatestSign(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Long classId) {
        if (!ClassPermissionCheckUtil.isStudentInClassByClassId(classId)) {
            return null;
        }
        return studentService.getLatestSignByClassId(classId);
    }

    @GroupMeta(permission = "学生进行签到", module = "学生", mount = true)
    @PostMapping("/sign/confirm/{signId}")
    public UnifyResponseVO confirmStudentSign(
            @Min(value = 1, message = "{lesson.sign.id.positive}")
            @PathVariable Long signId,
            HttpServletRequest request) {
        if (!ClassPermissionCheckUtil.isStudentInClassBySignId(signId)) {
            return ResponseUtil.generateUnifyResponse(10205);
        }
        if (!studentService.signAvailable(signId)) {
            return ResponseUtil.generateUnifyResponse(10211);
        }
        String ip = IPUtil.getIPFromRequest(request);
        if (!studentService.confirmSign(signId, ip)) {
            return ResponseUtil.generateUnifyResponse(10210);
        }
        return ResponseUtil.generateUnifyResponse(20);
    }

//    @GetMapping("")
//    public List<BookDO> getBooks() {
//        List<BookDO> books = bookService.findAll();
//        return books;
//    }
//
//
//    @GetMapping("/search")
//    public List<BookDO> searchBook(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
//        List<BookDO> books = bookService.getBookByKeyword("%" + q + "%");
//        return books;
//    }
//
//
//    @PostMapping("")
//    public UnifyResponseVO createBook(@RequestBody @Validated CreateOrUpdateBookDTO validator) {
//        bookService.createBook(validator);
//        return ResponseUtil.generateUnifyResponse(10);
//    }
//
//
//    @PutMapping("/{id}")
//    public UnifyResponseVO updateBook(@PathVariable("id") @Positive(message = "{id}") Long id, @RequestBody @Validated CreateOrUpdateBookDTO validator) {
//        BookDO book = bookService.getById(id);
//        if (book == null) {
//            throw new NotFoundException("book not found", 10022);
//        }
//        bookService.updateBook(book, validator);
//        return ResponseUtil.generateUnifyResponse(11);
//    }
//
//
//    @DeleteMapping("/{id}")
//    @GroupMeta(permission = "删除图书", module = "图书", mount = true)
//    public UnifyResponseVO deleteBook(@PathVariable("id") @Positive(message = "{id}") Long id) {
//        BookDO book = bookService.getById(id);
//        if (book == null) {
//            throw new NotFoundException("book not found", 10022);
//        }
//        bookService.deleteById(book.getId());
//        return ResponseUtil.generateUnifyResponse(12);
//    }


}
