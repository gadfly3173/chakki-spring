package vip.gadfly.chakkispring.dto.admin;

import io.github.talelin.autoconfigure.validator.Length;
import io.github.talelin.autoconfigure.validator.LongList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author gadfly
 */
@Data
public class NewClassDTO {
    @NotBlank(message = "{class.name.not-blank}")
    @Length(min = 1, max = 60, message = "{class.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{class.info.length}")
    private String info;
}
