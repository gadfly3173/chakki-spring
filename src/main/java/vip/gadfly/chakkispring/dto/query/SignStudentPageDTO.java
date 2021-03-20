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
@ApiModel(value = "根据签到id分页查询学生DTO")
public class SignStudentPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "签到状态")
    @Min(value = 0, message = "{sign-status}")
    private Integer signStatus = 0;

    @ApiModelProperty(value = "是否按IP排序")
    private Boolean orderByIP = false;

    @ApiModelProperty(value = "用户名")
    private String username = "";

}
