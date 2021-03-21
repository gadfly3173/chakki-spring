package vip.gadfly.chakkispring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Gadfly
 */

@Data
@ApiModel(value = "选项VO", description = "选项")
public class OptionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer order;

}
