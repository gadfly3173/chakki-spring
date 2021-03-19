package vip.gadfly.chakkispring.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
@ApiModel(value="批量注册用户DTO", description="批量注册")
public class BatchRegisterDTO {

    @ApiModelProperty(value = "用户列表", required = true)
    @Size(min = 1, message = "{user.register.batch.size}")
    @Valid
    private List<RegisterDTO> batchUsers;
}
