package vip.gadfly.chakkispring.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户签到信息 view object
 *
 * @author Gadfly
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentWorkVO {

    private Integer id;

    private Integer workId;

    private Integer userId;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户ip
     */
    private String ip;

    /**
     * 作业评分
     */
    private Integer rate;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
