package vip.gadfly.chakkispring.controller.cms;

import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import vip.gadfly.chakkispring.bo.FileBO;
import vip.gadfly.chakkispring.bo.FileExportBO;
import vip.gadfly.chakkispring.common.annotation.StudentClassCheck;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.FileService;

import javax.validation.constraints.Positive;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static vip.gadfly.chakkispring.common.constant.ClassVerifyConstant.*;

@Api(value = "/cms/file", tags = "文件管理及下载")
@Controller
@RequestMapping("/cms/file")
@Validated
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ClassService classService;

    /**
     * 上传共用图片
     *
     * @param multipartHttpServletRequest 携带文件的 request
     * @return 文件信息
     */
    @PostMapping("/img/upload")
    @ResponseBody
    @GroupRequired
    @PermissionMeta(value = "上传共用图片", module = "文件管理")
    public List<FileBO> upload(MultipartHttpServletRequest multipartHttpServletRequest) {
        MultiValueMap<String, MultipartFile> fileMap =
                multipartHttpServletRequest.getMultiFileMap();
        return fileService.upload(fileMap);
    }

    @GetMapping("/lesson/work/student/download/{id}")
    @GroupRequired
    @PermissionMeta(value = "下载指定学生作业文件", module = "教师")
    @TeacherClassCheck(valueType = studentWorkIdType, paramType = pathVariableType)
    public ResponseEntity<FileSystemResource> downloadStudentWorkFile(
            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        File file = classService.getStudentWorkFile(id);
        String filename = classService.getStudentWorkFilename(id);
        return ResponseUtil.generateFileResponse(file, filename);
    }

    @GetMapping("/lesson/work/download/{id}")
    @GroupRequired
    @PermissionMeta(value = "下载指定作业项目的全部文件", module = "教师")
    @TeacherClassCheck(valueType = workIdType, paramType = pathVariableType)
    public ResponseEntity<FileSystemResource> downloadWorkFile(
            @PathVariable @Positive(message = "{id.positive}") Integer id) throws IOException {
        File file = classService.workFilesToZip(id);
        String filename = classService.getWorkZipFilename(id);
        return ResponseUtil.generateFileResponse(file, filename);
    }

    @GetMapping("/lesson/announcement/download/{id}")
    @GroupRequired
    @PermissionMeta(value = "下载公告附件", module = "教师")
    @TeacherClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public ResponseEntity<FileSystemResource> downloadAnnouncementFile(
            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        File file = classService.getAnnouncementFile(id);
        String filename = classService.getAnnouncementFilename(id);
        return ResponseUtil.generateFileResponse(file, filename);
    }

    @GetMapping("/class/announcement/download/{id}")
    @GroupRequired
    @PermissionMeta(value = "下载公告附件", module = "学生")
    @StudentClassCheck(valueType = announcementIdType, paramType = pathVariableType)
    public ResponseEntity<FileSystemResource> downloadStudentAnnouncementFile(
            @PathVariable @Positive(message = "{id.positive}") Integer id) {
        File file = classService.getAnnouncementFile(id);
        String filename = classService.getAnnouncementFilename(id);
        return ResponseUtil.generateFileResponse(file, filename);
    }

    @GetMapping("/lesson/questionnaire/download/{id}")
    @GroupRequired
    @PermissionMeta(value = "下载问卷收集结果", module = "教师")
    @TeacherClassCheck(valueType = questionnaireIdType, paramType = pathVariableType)
    public ResponseEntity<FileSystemResource> downloadStudentQuestionnaireReport(
            @PathVariable @Positive(message = "{id.positive}") Integer id) throws IOException {
        FileExportBO exportBO = classService.getQuestionnaireReportFile(id);
        return ResponseUtil.generateFileResponse(exportBO.getFile(), exportBO.getFilename());
    }
}
