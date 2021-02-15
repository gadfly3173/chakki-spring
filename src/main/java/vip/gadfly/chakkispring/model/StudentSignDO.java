package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gadfly
 */
@TableName("student_sign")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSignDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 签到id
     */
    private Integer signId;

    /**
     * 签到ip
     */
    private String ip;

    /**
     * 签到状态
     */
    private Integer status;

    private Date createTime;

    public StudentSignDO(Integer signId, Integer userId, String ip, Integer status) {
        this.userId = userId;
        this.signId = signId;
        this.ip = ip;
        this.status = status;
    }
}
