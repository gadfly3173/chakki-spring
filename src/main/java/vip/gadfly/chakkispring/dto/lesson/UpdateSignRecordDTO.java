package vip.gadfly.chakkispring.dto.lesson;

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
public class UpdateSignRecordDTO {

    @NotNull(message = "{id}")
    @Min(value = 1, message = "{id}")
    private Integer userId;

    @Min(value = SignStatusConstant.STATUS_SIGNED, message = "{lesson.sign.status}")
    @Max(value = SignStatusConstant.STATUS_CANCEL, message = "{lesson.sign.status}")
    private Integer signStatus;

}
