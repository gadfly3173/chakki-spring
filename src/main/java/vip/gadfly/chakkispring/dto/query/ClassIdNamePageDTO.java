package vip.gadfly.chakkispring.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "根据班级id和用户名分页查询DTO")
public class ClassIdNamePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "班级id", required = true)
    @NotNull(message = "{class-id}")
    @Min(value = 1, message = "{class-id}")
    private Integer classId;

    @ApiModelProperty(value = "名字", required = true)
    @NotBlank(message = "{search-text.blank}")
    private String name;
}
