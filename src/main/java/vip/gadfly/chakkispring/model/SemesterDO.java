package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author gadfly
 */
@Data
@Builder
@TableName("semester")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SemesterDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学期名
     */
    private String name;

    /**
     * 学期信息
     */
    private String info;
}
