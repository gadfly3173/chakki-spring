package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("student_questionnaire")
@EqualsAndHashCode(callSuper = true)
public class StudentQuestionnaireDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 问卷id
     */
    private Integer questionnaireId;

    /**
     * ip
     */
    private String ip;

}
