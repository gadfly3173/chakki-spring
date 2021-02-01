package vip.gadfly.chakkispring.dto.admin;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author gadfly
 */
@Data
public class NewClassDTO {
    @NotBlank(message = "{class.name.not-blank}")
    @Length(min = 1, max = 60, message = "{class.name.length}")
    private String name;

    @Length(max = 255, message = "{class.info.length}")
    private String info;

    @Min(value = 1, message = "{semester.id.positive}")
    private Integer semesterId;
}
