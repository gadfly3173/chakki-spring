package vip.gadfly.chakkispring.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
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
    public List<ClassDO> getClassesBySemesterAndStudent(
            @ApiParam(value = "学期id", required = true) @RequestParam("semester_id") Integer semesterId) {
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

    @ApiOperation(value = "查看班级最新签到项目", notes = "查看班级最新签到项目")
    @GroupRequired
    @PermissionMeta(value = "查看班级最新签到项目")
    @GetMapping("/sign/latest")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
    public SignListVO getLatestSign(
            @ApiParam(value = "班级id", required = true)
            @RequestParam(name = "class_id")
            @Positive(message = "{class-id}") Integer classId) {
        return studentService.getLatestSignByClassId(classId);
    }

    @ApiOperation(value = "学生进行签到", notes = "学生进行签到")
    @GroupRequired
    @PermissionMeta(value = "学生进行签到")
    @PostMapping("/sign/confirm/{signId}")
    @StudentClassCheck(valueType = signIdType, paramType = pathVariableType)
    public UnifyResponseVO<String> confirmStudentSign(
            @ApiParam(value = "签到id", required = true)
            @Positive(message = "{lesson.sign.id.positive}")
            @PathVariable Integer signId,
            @ApiIgnore HttpServletRequest request) {
        if (!studentService.signAvailable(signId)) {
            return ResponseUtil.generateUnifyResponse(10211);
        }
        String ip = IPUtil.getIPFromRequest(request);
        if (!studentService.confirmSign(signId, ip)) {
            return ResponseUtil.generateUnifyResponse(10210);
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
    public PageResponseVO<WorkVO> getWorkList(
            @ApiParam(value = "班级id", required = true) @RequestParam(name = "class_id")
            @Min(value = 1, message = "{class-id}") Integer classId,
            @ApiParam(value = "每页数量") @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Integer count,
            @ApiParam(value = "页数") @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Integer page) {
        IPage<WorkVO> iPage = classService.getWorkPageForStudentByClassId(classId, count, page);
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
    public UnifyResponseVO<String> handStudentWork(
            @Positive(message = "{lesson.sign.id.positive}")
            @PathVariable Integer workId,
            @ApiIgnore MultipartHttpServletRequest multipartHttpServletRequest) {
        if (!studentService.workAvailable(workId)) {
            return ResponseUtil.generateUnifyResponse(10231);
        }
        String ip = IPUtil.getIPFromRequest(multipartHttpServletRequest);
        MultiValueMap<String, MultipartFile> fileMap =
                multipartHttpServletRequest.getMultiFileMap();
        if (!studentService.handStudentWork(workId, fileMap, ip)) {
            return ResponseUtil.generateUnifyResponse(10230);
        }
        return ResponseUtil.generateUnifyResponse(31);
    }

    @ApiOperation(value = "查看所有通知公告", notes = "查看所有通知公告")
    @GetMapping("/announcement/list")
    @GroupRequired
    @PermissionMeta(value = "查看所有通知公告")
    @StudentClassCheck(valueType = classIdType, paramType = requestParamType, valueName = "class_id")
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
    @StudentClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public AnnouncementVO getAnnouncementVO(@ApiParam(value = "公告id", required = true)
            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        return classService.getAnnouncementVO(id);
    }

}
