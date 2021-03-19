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
@ApiModel(value = "签到项目VO", description = "签到项目VO")
public class SignListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 班级id
     */
    @ApiModelProperty(value = "班级id")
    private Integer classId;

    @ApiModelProperty(value = "签到项目名称")
    private String name;

    @ApiModelProperty(value = "签到人数 / 是否签到")
    private Integer signed;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}
