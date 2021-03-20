package vip.gadfly.chakkispring.dto.lesson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.gadfly.chakkispring.common.constant.SignStatusConstant;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value = "更新签到记录DTO", description = "更新签到记录")
public class UpdateSignRecordDTO {

    @ApiModelProperty(value = "学生id", required = true)
    @NotNull(message = "{id.positive}")
    @Min(value = 1, message = "{id.positive}")
    private Integer userId;

    @ApiModelProperty(value = "签到状态", required = true)
    @Min(value = SignStatusConstant.STATUS_SIGNED, message = "{lesson.sign.status}")
    @Max(value = SignStatusConstant.STATUS_CANCEL, message = "{lesson.sign.status}")
    private Integer signStatus;

}
