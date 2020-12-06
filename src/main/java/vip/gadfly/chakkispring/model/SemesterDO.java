package vip.gadfly.chakkispring.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gadfly
 */
@Data
@Builder
@TableName("semester")
@NoArgsConstructor
@AllArgsConstructor
public class SemesterDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学期名
     */
    private String name;

    /**
     * 学期信息
     */
    private String info;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @JsonIgnore
    @TableLogic
    private Date deleteTime;
}
