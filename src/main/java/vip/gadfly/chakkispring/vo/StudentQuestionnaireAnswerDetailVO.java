package vip.gadfly.chakkispring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Gadfly
 */
@Data
@ApiModel(value = "学生问卷回答详情VO", description = "问卷回答详情")
public class StudentQuestionnaireAnswerDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问题id")
    private Integer questionId;

    @ApiModelProperty(value = "问题")
    private String question;

    @ApiModelProperty(value = "回答选项列表")
    private List<Integer> optionId;

    @ApiModelProperty(value = "回答简答")
    private String answer;

}
