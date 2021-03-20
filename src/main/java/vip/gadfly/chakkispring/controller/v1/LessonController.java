package vip.gadfly.chakkispring.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;
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

@Api(value = "/v1/lesson", tags = "教师接口")
@RestController
@RequestMapping("/v1/lesson")
@PermissionModule(value = "教师")
@Validated
public class LessonController {

    @Autowired
    private ClassService classService;

    /**
     * 班级接口
     */
    @ApiOperation(value = "查询教师本学期所属班级", notes = "查询教师本学期所属班级")
    @GetMapping("/class/list")
    @GroupRequired
    @PermissionMeta(value = "查询教师本学期所属班级")
    public List<ClassDO> getClassesBySemesterAndTeacher(@ApiParam(value = "学期id", required = true)
                                                        @RequestParam("semester_id") Integer semesterId) {
        Integer teacherId = LocalUser.getLocalUser().getId();
        return classService.getClassesBySemesterAndTeacher(semesterId, teacherId);
    }

    @ApiOperation(value = "查询一个班级", notes = "查询一个班级")
    @GetMapping("/class/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个班级")
    @TeacherClassCheck(valueType = classIdType, paramType = pathVariableType)
    public ClassDO getClass(@ApiParam(value = "班级id", required = true)
                            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getClass(id);
    }

    @ApiOperation(value = "查询所有此班级学生", notes = "查询所有此班级学生")
    @GetMapping("/students")
    @GroupRequired
    @PermissionMeta(value = "查询所有此班级学生")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<UserDO> getStudents(
            @ApiParam(value = "班级id", required = true) @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @ApiParam(value = "每页数量") @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数") @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<UserDO> iPage = classService.getUserPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "发起签到", notes = "发起签到")
    @PostMapping("/sign/create")
    @GroupRequired
    @PermissionMeta(value = "发起签到")
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public UnifyResponseVO<String> createStudentSign(@RequestBody @Validated NewSignDTO validator) {
        classService.createSign(validator);
        return ResponseUtil.generateUnifyResponse(19);
    }

    @ApiOperation(value = "查看所有签到项目", notes = "查看所有签到项目")
    @GetMapping("/sign/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有签到项目")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<SignListVO> getSignList(
            @ApiParam(value = "班级id", required = true) @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @ApiParam(value = "每页数量") @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数") @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<SignListVO> iPage = classService.getSignPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查询单个签到项目下的所有学生", notes = "查询单个签到项目下的所有学生")
    @GetMapping("/sign/students/query/{signId}")
    @GroupRequired
    @PermissionMeta(value = "查询单个签到项目下的所有学生")
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public PageResponseVO<StudentSignVO> getStudentsBySignId(
            @ApiParam(value = "签到状态")
            @RequestParam(name = "sign_status", required = false, defaultValue = "0")
            @Min(value = 0, message = "{sign-status}") Integer signStatus,
            @ApiParam(value = "是否按IP排序")
            @RequestParam(name = "order_by_IP", required = false, defaultValue = "false")
                    boolean orderByIP,
            @ApiParam(value = "每页数量")
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数")
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page,
            @ApiParam(value = "用户名")
            @RequestParam(name = "username", required = false, defaultValue = "")
                    String username,
            @ApiParam(value = "签到id", required = true)
            @Min(value = 1, message = "{lesson.sign.id.positive}")
            @PathVariable Integer signId) {
        IPage<StudentSignVO> iPage = classService.getUserPageBySignId(signId, signStatus, username, count, page,
                orderByIP);
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查询一个签到信息", notes = "查询一个签到信息")
    @GetMapping("/sign/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个签到信息")
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public SignCountVO getSign(@ApiParam(value = "签到id", required = true)
                               @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getSign(id);
    }

    @ApiOperation(value = "修改签到记录", notes = "修改签到记录")
    @PostMapping("/sign/record/update/{signId}")
    @GroupRequired
    @PermissionMeta(value = "修改签到记录")
    @TeacherClassCheck(valueType = signIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> updateStudentSignRecord(@RequestBody @Validated UpdateSignRecordDTO validator,
                                                           @ApiParam(value = "签到id", required = true)
                                                           @PathVariable Integer signId) {
        if (classService.updateSignRecord(validator, signId)) {
            return ResponseUtil.generateUnifyResponse(21);
        }
        return ResponseUtil.generateUnifyResponse(10212);
    }

    @ApiOperation(value = "教师查询所有学期", notes = "教师查询所有学期")
    @GetMapping("/semester/all")
    @GroupRequired
    @PermissionMeta(value = "教师查询所有学期")
    public List<SemesterDO> getAllSemesters() {
        return classService.getAllSemesters();
    }

    @ApiOperation(value = "发起作业", notes = "发起作业")
    @PostMapping("/work/create")
    @GroupRequired
    @PermissionMeta(value = "发起作业")
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public UnifyResponseVO<String> createWork(@RequestBody @Validated NewWorkDTO validator) {
        classService.createWork(validator);
        return ResponseUtil.generateUnifyResponse(28);
    }

    @ApiOperation(value = "删除作业", notes = "删除作业")
    @PostMapping("/work/delete/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除作业")
    @TeacherClassCheck(valueType = workIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> deleteWork(@ApiParam(value = "作业id", required = true)
                                              @PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.deleteWork(id);
        return ResponseUtil.generateUnifyResponse(29);
    }

    @ApiOperation(value = "查看所有作业项目", notes = "查看所有作业项目")
    @GetMapping("/work/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有作业项目")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<WorkVO> getWorkList(
            @ApiParam(value = "班级id", required = true) @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @ApiParam(value = "每页数量") @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数") @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<WorkVO> iPage = classService.getWorkPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "修改作业", notes = "修改作业")
    @PostMapping("/work/update")
    @GroupRequired
    @PermissionMeta(value = "修改作业")
    @TeacherClassCheck(valueType = workIdType, paramType = requestBodyType, valueName = "id")
    public UnifyResponseVO<String> updateWork(@RequestBody @Validated UpdateWorkDTO validator) {
        classService.updateWork(validator);
        return ResponseUtil.generateUnifyResponse(30);
    }

    @ApiOperation(value = "查询单个作业项目下的所有学生", notes = "查询单个作业项目下的所有学生")
    @GetMapping("/work/students/query/{workId}")
    @GroupRequired
    @PermissionMeta(value = "查询单个作业项目下的所有学生")
    @TeacherClassCheck(valueType = workIdType, paramType = pathVariableType)
    public PageResponseVO<StudentWorkVO> getStudentsByWorkId(
            @ApiParam(value = "作业状态", required = true)
            @RequestParam(name = "work_status", required = false, defaultValue = "0")
            @Min(value = 0, message = "{work-status}") Integer workStatus,
            @ApiParam(value = "是否按IP排序")
            @RequestParam(name = "order_by_IP", required = false, defaultValue = "false")
                    boolean orderByIP,
            @ApiParam(value = "每页数量")
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数")
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page,
            @ApiParam(value = "用户名")
            @RequestParam(name = "username", required = false, defaultValue = "")
                    String username,
            @ApiParam(value = "作业id", required = true)
            @Min(value = 1, message = "{id.positive}")
            @PathVariable Integer workId) {
        IPage<StudentWorkVO> iPage = classService.getUserPageByWorkId(workId, workStatus, username, count, page,
                orderByIP);
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查询一个作业信息", notes = "查询一个作业信息")
    @GetMapping("/work/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询一个作业信息")
    @TeacherClassCheck(valueType = workIdType, paramType = pathVariableType)
    public WorkCountVO getWorkDetail(@ApiParam(value = "作业id", required = true)
                                     @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getWorkDetail(id);
    }

    @ApiOperation(value = "给学生作业打分", notes = "给学生作业打分")
    @PostMapping("/work/student/rate/{id}")
    @GroupRequired
    @PermissionMeta(value = "给学生作业打分")
    @TeacherClassCheck(valueType = studentWorkIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> rateStudentWork(@RequestBody @Validated RateStudentWorkDTO validator,
                                                   @ApiParam(value = "学生作业id", required = true)
                                                   @PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.rateStudentWork(validator, id);
        return ResponseUtil.generateUnifyResponse(32);
    }

    @ApiOperation(value = "删除学生作业", notes = "删除学生作业")
    @PostMapping("/work/student/delete/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除学生作业")
    @TeacherClassCheck(valueType = studentWorkIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> deleteStudentWork(@ApiParam(value = "学生作业id", required = true)
                                                     @PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.deleteStudentWork(id);
        return ResponseUtil.generateUnifyResponse(33);
    }

    @ApiOperation(value = "发布通知公告", notes = "发布通知公告")
    @PostMapping("/announcement/create")
    @GroupRequired
    @PermissionMeta(value = "发布通知公告")
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public Integer createAnnouncement(@RequestBody @Validated NewAnnouncementDTO dto) {
        return classService.createAnnouncement(dto);
    }

    @ApiOperation(value = "修改通知公告", notes = "修改通知公告")
    @PostMapping("/announcement/update/{id}")
    @GroupRequired
    @PermissionMeta(value = "修改通知公告")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> updateAnnouncement(@ApiParam(value = "公告id", required = true)
                                                      @PathVariable @Positive(message = "{id.positive}") Integer id,
                                                      @RequestBody @Validated NewAnnouncementDTO dto) {
        classService.updateAnnouncement(id, dto);
        return ResponseUtil.generateUnifyResponse(38);
    }

    @ApiOperation(value = "修改公告文件", notes = "修改公告文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公告id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "file", value = "上传的文件", required = true,
                    paramType = "form", dataType = "__File", allowMultiple = true)})
    @PostMapping("/announcement/attachment/{id}")
    @GroupRequired
    @PermissionMeta(value = "修改公告文件")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> updateAnnouncementAttachment(
            @Min(value = 1, message = "{id.positive}")
            @PathVariable Integer id,
            @ApiIgnore MultipartHttpServletRequest multipartHttpServletRequest) {
        MultiValueMap<String, MultipartFile> fileMap =
                multipartHttpServletRequest.getMultiFileMap();
        if (!classService.updateAnnouncementAttachment(id, fileMap)) {
            throw new FailedException(10240);
        }
        return ResponseUtil.generateUnifyResponse(36);
    }

    @ApiOperation(value = "查看所有通知公告", notes = "查看所有通知公告")
    @GetMapping("/announcement/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有通知公告")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<AnnouncementVO> getAnnouncementList(
            @ApiParam(value = "班级id", required = true) @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @ApiParam(value = "每页数量") @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数") @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<AnnouncementVO> iPage = classService.getAnnouncementPageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查看单个通知公告", notes = "查看单个通知公告")
    @GetMapping("/announcement/{id}")
    @GroupRequired
    @PermissionMeta(value = "查看单个通知公告")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public AnnouncementVO getAnnouncementVO(@ApiParam(value = "公告id", required = true)
                                            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getAnnouncementVO(id);
    }

    @ApiOperation(value = "删除通知公告", notes = "删除通知公告")
    @PostMapping("/announcement/delete/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除通知公告")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> deleteAnnouncement(@ApiParam(value = "公告id", required = true)
                                                      @PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.deleteAnnouncement(id);
        return ResponseUtil.generateUnifyResponse(37);
    }

    @ApiOperation(value = "发布问卷", notes = "发布问卷")
    @PostMapping("/questionnaire/create")
    @GroupRequired
    @PermissionMeta(value = "发布问卷")
    @TeacherClassCheck(valueType = classIdType, paramType = requestBodyType, valueName = "class_id")
    public UnifyResponseVO<String> createQuestionnaire(@RequestBody @Validated NewQuestionnaireDTO dto) {
        classService.createQuestionnaire(dto);
        return ResponseUtil.generateUnifyResponse(39);
    }

    @ApiOperation(value = "查看所有问卷", notes = "查看所有问卷")
    @GetMapping("/questionnaire/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有问卷")
    @TeacherClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<QuestionnaireVO> getQuestionnaireList(
            @ApiParam(value = "班级id", required = true) @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @ApiParam(value = "每页数量") @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数") @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<QuestionnaireVO> iPage = classService.getQuestionnairePageByClassId(classId, count, page);
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "删除问卷", notes = "删除问卷")
    @PostMapping("/questionnaire/delete/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除问卷")
    @TeacherClassCheck(valueType = questionnaireIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> deleteQuestionnaire(@ApiParam(value = "问卷id", required = true)
                                                       @PathVariable @Positive(message = "{id.positive}") Integer id) {
        classService.deleteQuestionnaire(id);
        return ResponseUtil.generateUnifyResponse(40);
    }

}
