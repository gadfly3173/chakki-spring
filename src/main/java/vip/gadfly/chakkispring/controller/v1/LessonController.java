package vip.gadfly.chakkispring.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;
import vip.gadfly.chakkispring.common.util.PageUtil;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.lesson.*;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.vo.*;

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

    @GetMapping("/class/list")
    @GroupRequired
    @PermissionMeta(value = "查询教师本学期所属班级")
    public List<ClassDO> getClassesBySemesterAndTeacher(@RequestParam("semester_id") Integer semesterId) {
        Integer teacherId = LocalUser.getLocalUser().getId();
        return classService.getClassesBySemesterAndTeacher(semesterId, teacherId);
    }

    @GetMapping("/class/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个班级")
    @TeacherClassCheck(valueType = classIdType, paramType = pathVariableType)
    public ClassDO getClass(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getClass(id);
    }

    @GetMapping("/students")
    @GroupRequired
    @PermissionMeta(value = "查询所有此班级学生")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<UserDO> getStudents(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<UserDO> iPage = classService.getUserPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @PostMapping("/sign/create")
    @GroupRequired
    @PermissionMeta(value = "发起签到")
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public UnifyResponseVO<String> createStudentSign(@RequestBody @Validated NewSignDTO validator) {
        classService.createSign(validator);
        return ResponseUtil.generateUnifyResponse(19);
    }

    @GetMapping("/sign/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有签到项目")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<SignListVO> getSignList(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<SignListVO> iPage = classService.getSignPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @GetMapping("/sign/students/query/{signId}")
    @GroupRequired
    @PermissionMeta(value = "查询单个签到项目下的所有学生")
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public PageResponseVO<StudentSignVO> getStudentsBySignId(
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
            @PathVariable Integer signId) {
        IPage<StudentSignVO> iPage = classService.getUserPageBySignId(signId, signStatus, username, count, page,
                orderByIP);
        return PageUtil.build(iPage);
    }

    @GetMapping("/sign/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个签到信息")
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public SignCountVO getSign(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getSign(id);
    }

    @PostMapping("/sign/record/update/{signId}")
    @GroupRequired
    @PermissionMeta(value = "修改签到记录")
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> updateStudentSignRecord(@RequestBody @Validated UpdateSignRecordDTO validator,
                                                           @PathVariable Integer signId) {
        if (classService.updateSignRecord(validator, signId)) {
            return ResponseUtil.generateUnifyResponse(21);
        }
        return ResponseUtil.generateUnifyResponse(10212);
    }

    @GetMapping("/semester/all")
    @GroupRequired
    @PermissionMeta(value = "教师查询所有学期")
    public List<SemesterDO> getAllSemesters() {
        return classService.getAllSemesters();
    }

    @PostMapping("/work/create")
    @GroupRequired
    @PermissionMeta(value = "发起作业")
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public UnifyResponseVO<String> createWork(@RequestBody @Validated NewWorkDTO validator) {
        classService.createWork(validator);
        return ResponseUtil.generateUnifyResponse(28);
    }

    @PostMapping("/work/delete/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除作业")
    @TeacherClassCheck(valueType = workIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> deleteWork(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.deleteWork(id);
        return ResponseUtil.generateUnifyResponse(29);
    }

    @GetMapping("/work/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有作业项目")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<WorkVO> getWorkList(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<WorkVO> iPage = classService.getWorkPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @PostMapping("/work/update")
    @GroupRequired
    @PermissionMeta(value = "发起作业")
    @TeacherClassCheck(valueType = workIdType, paramType = requestBodyType, valueName = "id")
    public UnifyResponseVO<String> updateWork(@RequestBody @Validated UpdateWorkDTO validator) {
        classService.updateWork(validator);
        return ResponseUtil.generateUnifyResponse(30);
    }

    @GetMapping("/work/students/query/{workId}")
    @GroupRequired
    @PermissionMeta(value = "查询单个作业项目下的所有学生")
    @TeacherClassCheck(valueType = workIdType, paramType = pathVariableType)
    public PageResponseVO<StudentWorkVO> getStudentsByWorkId(
            @RequestParam(name = "work_status", required = false, defaultValue = "0")
            @Min(value = 0, message = "{work-status}") Integer workStatus,
            @RequestParam(name = "order_by_IP", required = false, defaultValue = "false")
                    boolean orderByIP,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page,
            @RequestParam(name = "username", required = false, defaultValue = "")
                    String username,
            @Min(value = 1, message = "{id.positive}")
            @PathVariable Integer workId) {
        IPage<StudentWorkVO> iPage = classService.getUserPageByWorkId(workId, workStatus, username, count, page,
                orderByIP);
        return PageUtil.build(iPage);
    }

    @GetMapping("/work/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个作业信息")
    @TeacherClassCheck(valueType = workIdType, paramType = pathVariableType)
    public WorkCountVO getWorkDetail(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getWorkDetail(id);
    }

    @PostMapping("/work/student/rate/{id}")
    @GroupRequired
    @PermissionMeta(value = "给学生作业打分")
    @TeacherClassCheck(valueType = studentWorkIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> rateStudentWork(@RequestBody @Validated RateStudentWorkDTO validator,
                                                   @PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.rateStudentWork(validator, id);
        return ResponseUtil.generateUnifyResponse(32);
    }

    @PostMapping("/work/student/delete/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除学生作业")
    @TeacherClassCheck(valueType = studentWorkIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> deleteStudentWork(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.deleteStudentWork(id);
        return ResponseUtil.generateUnifyResponse(33);
    }

    @PostMapping("/announcement/create")
    @GroupRequired
    @PermissionMeta(value = "发布通知公告")
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public Integer createAnnouncement(@RequestBody @Validated NewAnnouncementDTO dto) {
        return classService.createAnnouncement(dto);
    }

    @PostMapping("/announcement/update/{id}")
    @GroupRequired
    @PermissionMeta(value = "修改通知公告")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> updateAnnouncement(@PathVariable @Positive(message = "{id.positive}") Integer id,
                                                      @RequestBody @Validated NewAnnouncementDTO dto) {
        classService.updateAnnouncement(id, dto);
        return ResponseUtil.generateUnifyResponse(38);
    }

    @PostMapping("/announcement/attachment/{id}")
    @GroupRequired
    @PermissionMeta(value = "修改公告文件")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> updateAnnouncementAttachment(
            @Min(value = 1, message = "{id.positive}")
            @PathVariable Integer id,
            MultipartHttpServletRequest multipartHttpServletRequest) {
        MultiValueMap<String, MultipartFile> fileMap =
                multipartHttpServletRequest.getMultiFileMap();
        if (!classService.updateAnnouncementAttachment(id, fileMap)) {
            throw new FailedException(10240);
        }
        return ResponseUtil.generateUnifyResponse(36);
    }

    @GetMapping("/announcement/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有通知公告")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<AnnouncementVO> getAnnouncementList(
            @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<AnnouncementVO> iPage = classService.getAnnouncementPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @GetMapping("/announcement/{id}")
    @GroupRequired
    @PermissionMeta(value = "查看单个通知公告")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public AnnouncementVO getAnnouncementVO(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getAnnouncementVO(id);
    }

    @PostMapping("/announcement/delete/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除通知公告")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> deleteAnnouncement(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.deleteAnnouncement(id);
        return ResponseUtil.generateUnifyResponse(37);
    }
}
