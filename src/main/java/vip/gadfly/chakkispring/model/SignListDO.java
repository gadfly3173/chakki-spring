package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gadfly
 */
@TableName("sign_list")
@Data
public class SignListDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 班级id
     */
    private Integer classId;

    private String name;

    private Date createTime;

    private Date endTime;

}
