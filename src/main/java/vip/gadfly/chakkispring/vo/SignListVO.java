package vip.gadfly.chakkispring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Data
public class SignListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 班级id
     */
    private Integer classId;

    private String name;

    private Integer signed;

    private Date createTime;

    private Date endTime;

}
