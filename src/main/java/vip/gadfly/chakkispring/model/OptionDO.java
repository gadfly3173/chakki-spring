package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 问题项目表
 * </p>
 *
 * @author Gadfly
 * @since 2021-02-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("option")
@EqualsAndHashCode(callSuper = true)
public class OptionDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问卷标题
     */
    private String title;

    /**
     * 对应问题
     */
    private Integer questionId;

    /**
     * 序号
     */
    private Integer order;

}
