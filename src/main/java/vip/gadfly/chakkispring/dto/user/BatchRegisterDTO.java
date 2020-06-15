package vip.gadfly.chakkispring.dto.user;

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
public class BatchRegisterDTO {

    @Size(min = 1, message = "{user.register.batch.size}")
    @Valid
    private List<RegisterDTO> batchUsers;
}
