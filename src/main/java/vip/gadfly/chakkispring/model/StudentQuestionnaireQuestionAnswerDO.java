package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author Gadfly
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("student_questionnaire_question_answer")
@EqualsAndHashCode(callSuper = true)
public class StudentQuestionnaireQuestionAnswerDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应的学生提交信息id
     */
    private Integer studentQuestionnaireId;

    /**
     * 问题id
     */
    private Integer questionId;

    /**
     * 文字回答
     */
    private String answer;

    /**
     * 选项id
     */
    private Integer optionId;

}
