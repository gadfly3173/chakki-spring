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
public class StudentQuestionnaireVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "问卷标题")
    private String title;

    @ApiModelProperty(value = "学生列表")
    private List<StudentQuestionnaireAnswerVO> list;

    private Date createTime;

}
