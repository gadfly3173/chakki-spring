package vip.gadfly.chakkispring.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pedro
 * @since 2019-11-30
 */
@TableName("sign_list")
@Data
public class SignListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 班级id
     */
    private Long classId;

    private String name;

    private Long signed;

    private Date createTime;

    private Date endTime;

}
