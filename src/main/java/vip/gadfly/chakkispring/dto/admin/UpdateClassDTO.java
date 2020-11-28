package vip.gadfly.chakkispring.dto.admin;

import io.github.talelin.autoconfigure.validator.Length;
import lombok.Data;

/**
 * @author gadfly
 */
@Data
public class UpdateClassDTO {

    @Length(min = 1, max = 60, message = "{class.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{class.info.length}")
    private String info;
}