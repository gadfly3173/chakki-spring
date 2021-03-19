package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author gadfly
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("class")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "班级DO", description = "班级")
public class ClassDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级名称，例如：搬砖者
     */
    @ApiModelProperty(value = "班级名字")
    private String name;

    /**
     * 班级信息：例如：搬砖的人
     */
    @ApiModelProperty(value = "班级简介")
    private String info;

    /**
     * 学期id
     */
    @ApiModelProperty(value = "学期id")
    private Integer semesterId;
}
