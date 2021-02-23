package vip.gadfly.chakkispring.controller.cms;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import vip.gadfly.chakkispring.bo.FileBO;
import vip.gadfly.chakkispring.common.annotation.TeacherClassCheck;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.FileService;

import javax.validation.constraints.Positive;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static vip.gadfly.chakkispring.common.constant.ClassVerifyConstant.*;

@Controller
@RequestMapping("/cms/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ClassService classService;

    /**
     * 文件上传
     *
     * @param multipartHttpServletRequest 携带文件的 request
     * @return 文件信息
     */
    // @PostMapping("")
    @ResponseBody
    @GroupRequired
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
        return export(file, filename);
    }

    private ResponseEntity<FileSystemResource> export(File file, String filename) {
        if (!file.canRead()) {
            throw new NotFoundException(10020);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noStore().mustRevalidate());
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build());
        headers.setPragma("no-cache");
        headers.setExpires(0L);
        headers.setLastModified(System.currentTimeMillis());
        headers.setETag("\"" + System.currentTimeMillis() + "\"");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
    }

}
