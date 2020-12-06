package vip.gadfly.chakkispring.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息 view object
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherClassVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 教师级别：1-主教师， 2-助教、辅导员等
     */
    private Integer level;
}
