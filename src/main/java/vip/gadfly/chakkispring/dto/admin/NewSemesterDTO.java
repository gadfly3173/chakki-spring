package vip.gadfly.chakkispring.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author Gadfly
 */

@Setter
@Getter
@NoArgsConstructor
public class NewSemesterDTO {
    @NotBlank(message = "{semester.name.not-blank}")
    @Length(min = 1, max = 60, message = "{semester.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{semester.info.length}")
    private String info;
}
