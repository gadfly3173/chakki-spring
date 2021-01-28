package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * @author gadfly
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("class")
@EqualsAndHashCode(callSuper = true)
public class ClassDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级名称，例如：搬砖者
     */
    private String name;

    /**
     * 班级信息：例如：搬砖的人
     */
    private String info;

    /**
     * 学期id
     */
    private Long semesterId;
}
