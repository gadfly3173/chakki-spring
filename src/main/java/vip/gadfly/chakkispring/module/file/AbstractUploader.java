package vip.gadfly.chakkispring.module.file;

import io.github.talelin.autoconfigure.exception.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传类的基类
 * 模版模式
 */
public abstract class AbstractUploader implements Uploader {

    private PreHandler preHandler;

    @Override
    public List<File> upload(MultiValueMap<String, MultipartFile> fileMap, List<String> include, List<String> exclude, Long singleFileLimit, int fileNum) {
        checkFileMap(fileMap, fileNum);
        // 得到单个文件的大小限制
        // 本地存储需先初始化存储文件夹
        return handleMultipartFiles(fileMap, include, exclude, singleFileLimit);
    }

    @Override
    public List<File> upload(MultiValueMap<String, MultipartFile> fileMap, PreHandler preHandler, List<String> include, List<String> exclude, Long singleFileLimit, int fileNum) {
        this.preHandler = preHandler;
        return this.upload(fileMap, include, exclude, singleFileLimit, fileNum);
    }

    protected List<File> handleMultipartFiles(MultiValueMap<String, MultipartFile> fileMap, List<String> include, List<String> exclude, Long singleFileLimit) {
        List<File> res = new ArrayList<>();
        fileMap.keySet().forEach(key -> fileMap.get(key).forEach(file -> {
            if (!file.isEmpty()) {
                handleOneFile0(res, singleFileLimit, file, include, exclude);
            }
        }));
        return res;
    }

    private void handleOneFile0(List<File> res, long singleFileLimit, MultipartFile file, List<String> include, List<String> exclude) {
        byte[] bytes = getFileBytes(file);
        String ext = checkOneFile(include, exclude, singleFileLimit, file.getOriginalFilename(), bytes.length);
        String newFilename = getNewFilename(ext);
        String storePath = getStorePath(newFilename);
        // 生成文件的md5值
        String md5 = FileUtil.getFileMD5(bytes);
        File fileData = File.builder().
                name(newFilename).
                md5(md5).
                key(file.getOriginalFilename()).
                path(storePath).
                size(bytes.length).
                type(getFileType()).
                extension(ext).
                build();
        // 如果预处理器不为空，且处理结果为false，直接返回, 否则处理
        if (preHandler != null && !preHandler.handle(fileData)) {
            return;
        }
        boolean ok = handleOneFile(bytes, newFilename);
        if (ok) {
            res.add(fileData);
        }
    }

    private long getSingleFileLimit() {
        String singleLimit = getFileProperties().getSingleLimit();
        return FileUtil.parseSize(singleLimit);
    }

    /**
     * 得到文件配置
     *
     * @return 文件配置
     */
    protected abstract FileProperties getFileProperties();

    /**
     * 处理一个文件
     */
    protected abstract boolean handleOneFile(byte[] bytes, String newFilename);

    /**
     * 返回文件路径
     *
     * @param newFilename 文件名
     * @return 文件路径
     */
    protected abstract String getStorePath(String newFilename);

    /**
     * 返回文件存储位置类型
     *
     * @return LOCAL ｜ REMOTE
     */
    protected abstract String getFileType();

    /**
     * 获得新文件的名称
     *
     * @param ext 文件后缀
     * @return 新名称
     */
    protected String getNewFilename(String ext) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + "." + ext;
    }

    /**
     * 检查文件
     */
    protected void checkFileMap(MultiValueMap<String, MultipartFile> fileMap, int fileNum) {
        if (fileMap.isEmpty()) {
            throw new NotFoundException(10026);
        }
        if (fileMap.size() > fileNum) {
            throw new FileTooManyException(10121);
        }
    }

    /**
     * 获得文件的字节
     *
     * @param file 文件
     * @return 字节
     */
    protected byte[] getFileBytes(MultipartFile file) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (Exception e) {
            throw new FailedException(10190, "read file date failed");
        }
        return bytes;
    }

    /**
     * 单个文件检查
     *
     * @param singleFileLimit 单个文件大小限制
     * @param originName      文件原始名称
     * @param length          文件大小
     * @return 文件的扩展名，例如： .jpg
     */
    protected String checkOneFile(List<String> include, List<String> exclude, long singleFileLimit, String originName, int length) {
        // 写到了本地
        String ext = FileUtil.getFileExt(originName).toUpperCase();
        // 检测扩展
        if (!this.checkExt(include, exclude, ext)) {
            throw new FileExtensionException(ext + "文件类型不支持");
        }
        // 检测单个大小
        if (length > singleFileLimit) {
            throw new FileTooLargeException(originName + "文件不能超过" + singleFileLimit);
        }
        return ext;
    }

    /**
     * 检查文件后缀
     *
     * @param ext 后缀名
     * @return 是否通过
     */
    protected boolean checkExt(List<String> include, List<String> exclude, String ext) {
        int inLen = include == null ? 0 : include.size();
        int exLen = exclude == null ? 0 : exclude.size();
        // 如果两者都有取 include，有一者则用一者
        if (inLen > 0 && exLen > 0) {
            return this.findInInclude(include, ext);
        } else if (inLen > 0) {
            // 有include，无exclude
            return this.findInInclude(include, ext);
        } else if (exLen > 0) {
            // 有exclude，无include
            return this.findInExclude(exclude, ext);
        } else {
            // 二者都没有
            return true;
        }
    }

    protected boolean findInInclude(List<String> include, String ext) {
        for (String s : include) {
            if (s.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    protected boolean findInExclude(List<String> exclude, String ext) {
        for (String s : exclude) {
            if (s.equals(ext)) {
                return true;
            }
        }
        return false;
    }
}