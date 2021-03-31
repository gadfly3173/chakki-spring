package vip.gadfly.chakkispring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Gadfly
 */
@Data
@ApiModel(value = "学生问卷回答详情VO", description = "问卷回答详情")
public class StudentQuestionnaireAnswerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "回答列表")
    private List<StudentQuestionnaireAnswerDetailVO> answers;

    private Date createTime;

}
