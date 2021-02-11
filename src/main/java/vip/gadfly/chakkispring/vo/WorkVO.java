package vip.gadfly.chakkispring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Gadfly
 */
@Data
public class WorkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String info;

    private Integer fileNum;

    private Integer fileSize;

    private Integer type;

    private List<String> fileExtend;

    private Integer handed;

    private Date createTime;

    private Date updateTime;

    private Date endTime;

}
