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
@TableName("semester")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "学期DO", description = "学期DO")
public class SemesterDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学期名
     */
    @ApiModelProperty(value = "学期名字")
    private String name;

    /**
     * 学期信息
     */
    @ApiModelProperty(value = "学期简介")
    private String info;
}
