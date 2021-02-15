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

    private Integer fileSize;

    private Integer type;

    private List<String> fileExtension;

    /**
     * 教师查为交作业的学生数量，学生查为自己的有效提交次数
     */
    private Integer handed;

    private Date createTime;

    private Date updateTime;

    private Date endTime;

}
