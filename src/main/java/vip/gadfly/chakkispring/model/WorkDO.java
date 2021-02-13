package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

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
@TableName("work")
@EqualsAndHashCode(callSuper = true)
public class WorkDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作业名称，例如：搬砖
     */
    private String name;

    /**
     * 作业信息：例如：第一次板砖
     */
    private String info;

    /**
     * 对应班级
     */
    private Integer classId;

    /**
     * 单文件大小限制，单位为B/byte
     */
    private Integer fileSize;

    /**
     * 作业类型：1-课堂 2-回家
     */
    private Integer type;

    private Date endTime;
}
