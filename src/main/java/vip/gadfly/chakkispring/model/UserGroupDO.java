package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Data
@TableName("lin_user_group")
public class UserGroupDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 分组id
     */
    private Integer groupId;

    public UserGroupDO(Integer userId, Integer groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
