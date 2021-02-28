package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 作业项目表
 * </p>
 *
 * @author Gadfly
 * @since 2021-02-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("announcement")
@EqualsAndHashCode(callSuper = true)
public class AnnouncementDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 富文本内容
     */
    private String content;

    /**
     * 对应班级
     */
    private Integer classId;

    /**
     * 附件id
     */
    private Integer fileId;

    /**
     * 附件文件名
     */
    private String filename;

}
