package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 问题项目表
 * </p>
 *
 * @author Gadfly
 * @since 2021-02-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("question")
@EqualsAndHashCode(callSuper = true)
public class QuestionDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问卷标题
     */
    private String title;

    /**
     * 对应问卷
     */
    private Integer questionnaireId;

    /**
     * 序号
     */
    private Integer order;

    /**
     * 类型：1-简答 2-选择
     */
    private Integer type;

    /**
     * 选择数量限制
     */
    private Integer limitMax;

}
