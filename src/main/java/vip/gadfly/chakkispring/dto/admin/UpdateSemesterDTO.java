package vip.gadfly.chakkispring.dto.admin;

import org.hibernate.validator.constraints.Length;
import lombok.Data;

/**
 * @author gadfly
 */
@Data
public class UpdateSemesterDTO {

    @Length(min = 1, max = 60, message = "{semester.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{semester.info.length}")
    private String info;
}
