package vip.gadfly.chakkispring.extension.file;

import io.github.talelin.autoconfigure.exception.FailedException;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class FileUtil {

    public static FileSystem getDefaultFileSystem() {
        return FileSystems.getDefault();
    }

    public static boolean isAbsolute(String str) {
        Path path = getDefaultFileSystem().getPath(str);
        return path.isAbsolute();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initStoreDir(String dir) {
        String absDir;
        if (isAbsolute(dir)) {
            absDir = dir;
        } else {
            String cmd = getCmd();
            Path path = getDefaultFileSystem().getPath(cmd, dir);
            absDir = path.toAbsolutePath().toString();
        }
        File file = new File(absDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getCmd() {
        return System.getProperty("user.dir");
    }

    public static String getFileAbsolutePath(String dir, String filename) {
        if (isAbsolute(dir)) {
            return getDefaultFileSystem()
                    .getPath(dir, filename)
                    .toAbsolutePath().toString();
        } else {
            return getDefaultFileSystem()
                    .getPath(getCmd(), dir, filename)
                    .toAbsolutePath().toString();
        }
    }

    public static String getFileExt(String filename) {
        int index = filename.lastIndexOf('.');
        return filename.substring(index);
    }

    public static String getFileMD5(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }

    public static Long parseSize(String size) {
        DataSize singleLimitData = DataSize.parse(size);
        return singleLimitData.toBytes();
    }

    /**
     * 将文件头转换成16进制字符串
     *
     * @param
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static String getFileHead(MultipartFile file) throws IOException {

        byte[] b = new byte[28];

        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            String charset = UniversalDetector.detectCharset(inputStream);
            if (!StringUtils.isEmpty(charset)) {
                return charset;
            }
            if (inputStream.read(b, 0, b.length) == -1) {
                throw new FailedException("read file date failed", 10190);
            }
        }
        return bytesToHexString(b);
    }

    /**
     * 判断文件类型
     *
     * @param file 上传的文件
     * @return 文件类型
     */
    public static FileType getType(MultipartFile file) throws IOException {

        String fileHead = getFileHead(file);

        if (!StringUtils.isEmpty(fileHead)) {
            return null;
        }

        fileHead = fileHead.toUpperCase();

        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }

        return null;
    }

}
