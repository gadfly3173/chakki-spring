package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Gadfly
 */
@TableName("teacher_class")
@Data
public class TeacherClassDO implements Serializable {

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

    /**
     * 教师级别：1-主教师， 2-助教、辅导员等
     */
    private Integer level;

    public TeacherClassDO(Long classId, Long userId, Integer level) {
        this.userId = userId;
        this.classId = classId;
        this.level = level;
    }
}
