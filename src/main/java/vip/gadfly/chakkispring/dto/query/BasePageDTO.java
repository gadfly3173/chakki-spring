package vip.gadfly.chakkispring.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "基础分页查询DTO")
public class BasePageDTO {

    @ApiModelProperty(value = "每页数量")
    @Min(value = 1, message = "{count}")
    private Integer count = 10;

    @ApiModelProperty(value = "页数")
    @Min(value = 0, message = "{page}")
    private Integer page = 0;

}
