package vip.gadfly.chakkispring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据统一 view object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分页VO", description = "分页")
public class PageResponseVO<T> {

    @ApiModelProperty(value = "总数")
    private Integer total;

    @ApiModelProperty(value = "分页数据")
    private List<T> items;

    @ApiModelProperty(value = "页数")
    private Integer page;

    @ApiModelProperty(value = "每页数量")
    private Integer count;
}
