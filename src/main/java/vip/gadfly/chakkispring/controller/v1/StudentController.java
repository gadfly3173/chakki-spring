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
import vip.gadfly.chakkispring.common.annotation.StudentClassCheck;
import vip.gadfly.chakkispring.common.util.IPUtil;
import vip.gadfly.chakkispring.common.util.PageUtil;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.dto.query.ClassIdPageDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;

import static vip.gadfly.chakkispring.common.constant.ClassVerifyConstant.*;

/**
 * @author Gadfly
 */

@Api(value = "/v1/class", tags = "学生接口")
@RestController
@RequestMapping("/v1/class")
@PermissionModule(value = "学生")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassService classService;

    @ApiOperation(value = "查看自己所属班级", notes = "查看自己所属班级")
    @GroupRequired
    @PermissionMeta(value = "查看自己所属班级")
    @GetMapping("/list")
    public List<ClassDO> getClassList() {
        return studentService.getStudentClassList();
    }

    @ApiOperation(value = "查询学生本学期所属班级", notes = "查询学生本学期所属班级")
    @GetMapping("/class/list")
    @GroupRequired
    @PermissionMeta(value = "查询学生本学期所属班级")
    public List<ClassDO> getClassesBySemesterAndStudent(@ApiParam(value = "学期id", required = true)
                                                        @RequestParam("semester_id") Integer semesterId) {
        Integer userId = LocalUser.getLocalUser().getId();
        return classService.getClassesBySemesterAndStudent(semesterId, userId);
    }

    @ApiOperation(value = "查询特定班级", notes = "查询特定班级")
    @GetMapping("/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询特定班级")
    @StudentClassCheck(valueType = classIdType, paramType = pathVariableType)
    public ClassDO getClass(@ApiParam(value = "班级id", required = true)
                            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getClass(id);
    }

    // 停用
    @GroupRequired
    @PermissionMeta(value = "查看班级内签到项目")
    // @GetMapping("/sign/list")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<SignListVO> getSignList(@Validated ClassIdPageDTO classIdPageDTO) {
        IPage<SignListVO> iPage = classService.getSignPageByClassId(classIdPageDTO.getClassId(),
                classIdPageDTO.getCount(),
                classIdPageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查看班级最新签到项目", notes = "查看班级最新签到项目")
    @GroupRequired
    @PermissionMeta(value = "查看班级最新签到项目")
    @GetMapping("/sign/latest")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public SignListVO getLatestSign(@ApiParam(value = "班级id", required = true)
                                    @RequestParam(name = "class_id")
                                    @Positive(message = "{class-id}") Integer classId) {
        return studentService.getLatestSignByClassId(classId);
    }

    @ApiOperation(value = "学生进行签到", notes = "学生进行签到")
    @GroupRequired
    @PermissionMeta(value = "学生进行签到")
    @PostMapping("/sign/confirm/{signId}")
    @StudentClassCheck(valueType = signIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> confirmStudentSign(@ApiParam(value = "签到id", required = true)
                                                      @Positive(message = "{lesson.sign.id.positive}")
                                                      @PathVariable Integer signId,
                                                      @ApiIgnore HttpServletRequest request) {
        if (!studentService.signAvailable(signId)) {
            throw new FailedException(10211);
        }
        String ip = IPUtil.getIPFromRequest(request);
        if (!studentService.confirmSign(signId, ip)) {
            throw new FailedException(10210);
        }
        return ResponseUtil.generateUnifyResponse(20);
    }

    @ApiOperation(value = "学生查询所有学期", notes = "学生查询所有学期")
    @GroupRequired
    @GetMapping("/semester/all")
    @PermissionMeta(value = "学生查询所有学期")
    public List<SemesterDO> getAllSemesters() {
        return classService.getAllSemesters();
    }

    @ApiOperation(value = "查看班级内作业项目", notes = "查看班级内作业项目")
    @GroupRequired
    @PermissionMeta(value = "查看班级内作业项目")
    @GetMapping("/work/list")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<WorkVO> getWorkList(@Validated ClassIdPageDTO classIdPageDTO) {
        IPage<WorkVO> iPage = classService.getWorkPageForStudentByClassId(classIdPageDTO.getClassId(),
                classIdPageDTO.getCount(),
                classIdPageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查询作业详情", notes = "查询作业详情")
    @GetMapping("/work/{id}")
    @GroupRequired
    @PermissionMeta(value = "查询作业详情")
    @StudentClassCheck(valueType = workIdType, paramType = pathVariableType)
    public WorkVO getWorkDetailForStudent(@ApiParam(value = "作业id", required = true)
                                          @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getOneWorkForStudent(id);
    }

    @ApiOperation(value = "交作业", notes = "交作业")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workId", value = "作业id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "file", value = "上传的文件", required = true,
                    paramType = "form", dataType = "__File", allowMultiple = true)})
    @GroupRequired
    @PermissionMeta(value = "交作业")
    @PostMapping("/work/hand/{workId}")
    @StudentClassCheck(valueType = workIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> handStudentWork(@Positive(message = "{lesson.sign.id.positive}")
                                                   @PathVariable Integer workId,
                                                   @ApiIgnore MultipartHttpServletRequest multipartHttpServletRequest) {
        if (!studentService.workAvailable(workId)) {
            throw new FailedException(10231);
        }
        String ip = IPUtil.getIPFromRequest(multipartHttpServletRequest);
        MultiValueMap<String, MultipartFile> fileMap =
                multipartHttpServletRequest.getMultiFileMap();
        if (!studentService.handStudentWork(workId, fileMap, ip)) {
            throw new FailedException(10230);
        }
        return ResponseUtil.generateUnifyResponse(31);
    }

    @ApiOperation(value = "查看所有通知公告", notes = "查看所有通知公告")
    @GetMapping("/announcement/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有通知公告")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<AnnouncementVO> getAnnouncementList(@Validated ClassIdPageDTO classIdPageDTO) {
        IPage<AnnouncementVO> iPage = classService.getAnnouncementPageByClassId(classIdPageDTO.getClassId(),
                classIdPageDTO.getCount(),
                classIdPageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查看单个通知公告", notes = "查看单个通知公告")
    @GetMapping("/announcement/{id}")
    @GroupRequired
    @PermissionMeta(value = "查看单个通知公告")
    @StudentClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public AnnouncementVO getAnnouncementVO(@ApiParam(value = "公告id", required = true)
                                            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getAnnouncementVO(id);
    }

    @ApiOperation(value = "查看所有问卷", notes = "查看所有问卷")
    @GetMapping("/questionnaire/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有问卷")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public PageResponseVO<QuestionnairePageVO> getQuestionnaireList(@Validated ClassIdPageDTO classIdPageDTO) {
        IPage<QuestionnairePageVO> iPage = studentService.getQuestionnairePageForStudentByClassId(classIdPageDTO.getClassId(),
                classIdPageDTO.getCount(),
                classIdPageDTO.getPage());
        return PageUtil.build(iPage);
    }

    @ApiOperation(value = "查看单个问卷", notes = "查看单个问卷")
    @GetMapping("/questionnaire/{id}")
    @GroupRequired
    @PermissionMeta(value = "查看单个问卷")
    @StudentClassCheck(valueType = questionnaireIdType, paramType = pathVariableType)
    public QuestionnaireVO getQuestionnaireVO(@ApiParam(value = "问卷id", required = true)
                                              @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getQuestionnaireVO(id);
    }
}
