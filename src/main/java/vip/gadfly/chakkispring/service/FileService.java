package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import vip.gadfly.chakkispring.bo.FileBO;
import vip.gadfly.chakkispring.model.FileDO;

import java.util.List;

/**
 * @author pedro
 * @since 2019-11-30
 */
public interface FileService extends IService<FileDO> {

    /**
     * 上传文件
     *
     * @param fileMap 文件map
     * @return 文件数据
     */
    List<FileBO> upload(MultiValueMap<String, MultipartFile> fileMap);

    /**
     * 通过md5检查文件是否存在
     *
     * @param md5 md5
     * @return true 表示已存在
     */
    boolean checkFileExistByMd5(String md5);
}
