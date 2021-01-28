package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("lin_log")
@EqualsAndHashCode(callSuper = true)
public class LogDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = -7471474245124682011L;

    private String message;

    private Integer userId;

    private String username;

    private Integer statusCode;

    private String method;

    private String path;

    private String permission;
}
