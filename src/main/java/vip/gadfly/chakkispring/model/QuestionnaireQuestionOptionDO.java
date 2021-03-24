package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("questionnaire_question_option")
@EqualsAndHashCode(callSuper = true)
public class QuestionnaireQuestionOptionDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问卷标题
     */
    private String title;

    /**
     * 对应问题
     */
    private Integer questionId;

    /**
     * 序号
     */
    @TableField(value = "`order`")
    private Integer order;

}
