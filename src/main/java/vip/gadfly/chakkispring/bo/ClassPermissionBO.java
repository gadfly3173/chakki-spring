package vip.gadfly.chakkispring.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gadfly
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassPermissionBO {
    private Long id;

    private String name;

    private String info;
}
