package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Gadfly
 */
@TableName("student_class")
@Data
public class StudentClassDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 班级id
     */
    private Integer classId;

    public StudentClassDO(Integer classId, Integer userId) {
        this.userId = userId;
        this.classId = classId;
    }
}
