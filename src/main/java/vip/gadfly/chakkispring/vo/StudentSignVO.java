package vip.gadfly.chakkispring.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户签到信息 view object
 * @author Gadfly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentSignVO {

    private Long id;

    private Long signId;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 签到状态
     */
    private Integer signStatus;

    /**
     * 签到时间
     */
    private Date createTime;

}
