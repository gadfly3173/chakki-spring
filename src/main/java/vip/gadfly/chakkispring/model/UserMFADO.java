package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author pedro
 * @since 2019-12-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("lin_user_mfa")
@EqualsAndHashCode(callSuper = true)
public class UserMFADO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * MFA secret
     */
    private String secret;

}
