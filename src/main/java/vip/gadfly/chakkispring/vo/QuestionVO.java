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
@ApiModel(value = "问题VO", description = "问题")
public class QuestionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "排序")
    private Integer order;

    @ApiModelProperty(value = "上限，目前用于选择题", name = "limit_max")
    private Integer limitMax;

    @ApiModelProperty(value = "选项列表")
    private List<OptionVO> options;

}
