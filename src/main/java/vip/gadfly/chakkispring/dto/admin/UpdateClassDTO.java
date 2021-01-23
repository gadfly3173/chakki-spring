package vip.gadfly.chakkispring.dto.admin;

import org.hibernate.validator.constraints.Length;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author gadfly
 */
@Data
public class UpdateClassDTO {

    @Length(min = 1, max = 60, message = "{class.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{class.info.length}")
    private String info;

    @Min(value = 1, message = "{semester.id.positive}")
    private Long semesterId;
}
