package vip.gadfly.chakkispring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gadfly.chakkispring.model.WorkDO;

import java.io.Serializable;

/**
 * @author pedro
 * @since 2019-11-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WorkCountVO extends WorkDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer handed;
    private Integer unhanded;

}
