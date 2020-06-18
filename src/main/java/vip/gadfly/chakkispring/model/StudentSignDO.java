package vip.gadfly.chakkispring.model;

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
@TableName("student_sign")
@Data
public class StudentSignDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 签到id
     */
    private Long signId;

    private Date createTime;

    public StudentSignDO(Long signId, Long userId) {
        this.userId = userId;
        this.signId = signId;
    }
}
