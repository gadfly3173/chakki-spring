package vip.gadfly.chakkispring.bo;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class FileExportBO {

    /**
     * 文件
     */
    private File file;

    /**
     * 文件名
     */
    private String filename;

}
