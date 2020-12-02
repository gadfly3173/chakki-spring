package vip.gadfly.chakkispring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gadfly.chakkispring.model.SignListDO;

import java.io.Serializable;

/**
 * @author pedro
 * @since 2019-11-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SignCountVO extends SignListDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int signed;
    private int unSigned;
    private int late;
    private int cancel;

}
