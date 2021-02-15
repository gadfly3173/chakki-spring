package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author Gadfly
 */
@TableName("student_work")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentWorkDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 作业id
     */
    private Integer workId;

    /**
     * 签到ip
     */
    private String ip;

    /**
     * 文件id
     */
    private Integer fileId;

    /**
     * 教师评分
     */
    private Integer rate;

}
