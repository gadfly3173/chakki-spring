package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 问卷项目表
 * </p>
 *
 * @author Gadfly
 * @since 2021-02-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("questionnaire")
@EqualsAndHashCode(callSuper = true)
public class QuestionnaireDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问卷标题
     */
    private String title;

    /**
     * 简介
     */
    private String info;

    /**
     * 对应班级
     */
    private Integer classId;

    /**
     * 结束时间
     */
    private Date endTime;

}
