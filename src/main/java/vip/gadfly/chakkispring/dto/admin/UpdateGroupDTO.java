package vip.gadfly.chakkispring.dto.admin;

import org.hibernate.validator.constraints.Length;
import lombok.Data;

@Data
public class UpdateGroupDTO {

    @Length(min = 1, max = 60, message = "{group.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{group.info.length}")
    private String info;
}
