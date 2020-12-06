package vip.gadfly.chakkispring.controller.cms;

import io.github.talelin.core.annotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import vip.gadfly.chakkispring.bo.FileBO;
import vip.gadfly.chakkispring.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cms/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("")
    @LoginRequired
    public List<FileBO> upload(MultipartHttpServletRequest multipartHttpServletRequest) {
        MultiValueMap<String, MultipartFile> fileMap =
                multipartHttpServletRequest.getMultiFileMap();
        return fileService.upload(fileMap);
    }

}
