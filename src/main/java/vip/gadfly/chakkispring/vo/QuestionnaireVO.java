package vip.gadfly.chakkispring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gadfly
 */
@Data
public class QuestionnaireVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private String info;

    private Integer count;

    private Integer handed;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

}
