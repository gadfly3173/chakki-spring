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
@TableName("student_class")
@Data
public class StudentClassDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 班级id
     */
    private Long classId;

    public StudentClassDO(Long classId, Long userId) {
        this.userId = userId;
        this.classId = classId;
    }
}
