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
@ApiModel(value = "作业VO", description = "作业")
public class WorkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "作业名字")
    private String name;

    @ApiModelProperty(value = "作业简介")
    private String info;

    @ApiModelProperty(value = "允许上传的文件大小")
    private Integer fileSize;

    @ApiModelProperty(value = "作业类型")
    private Integer type;

    @ApiModelProperty(value = "允许上传的作业文件扩展名")
    private List<String> fileExtension;

    /**
     * 教师查为交作业的学生数量，学生查为自己的有效提交次数
     */
    @ApiModelProperty(value = "教师查为交作业的学生数量，学生查为自己的有效提交次数")
    private Integer handed;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}
