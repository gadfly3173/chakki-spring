package vip.gadfly.chakkispring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gadfly
 */
@Data
@ApiModel(value = "问卷VO", description = "问卷")
public class QuestionnaireVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "问卷标题")
    private String title;

    @ApiModelProperty(value = "问卷信息")
    private String info;

    @ApiModelProperty(value = "问题数量")
    private Integer count;

    @ApiModelProperty(value = "已作答人数 / 是否已作答")
    private Integer handed;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    private Date createTime;

    private Date updateTime;

}
