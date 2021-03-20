package vip.gadfly.chakkispring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户签到信息 view object
 *
 * @author Gadfly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "学生作业VO", description = "学生作业")
public class StudentWorkVO {

    private Integer id;

    @ApiModelProperty(value = "作业id")
    private Integer workId;

    @ApiModelProperty(value = "学生id")
    private Integer userId;

    /**
     * 用户名，唯一
     */
    @ApiModelProperty(value = "学生用户名")
    private String username;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "学生昵称")
    private String nickname;

    /**
     * 用户ip
     */
    @ApiModelProperty(value = "学生ip")
    private String ip;

    /**
     * 作业评分
     */
    @ApiModelProperty(value = "作业分数")
    private Integer rate;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
