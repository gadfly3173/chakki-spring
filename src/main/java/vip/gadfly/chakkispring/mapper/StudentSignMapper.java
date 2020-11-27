package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.StudentSignDO;
import vip.gadfly.chakkispring.vo.StudentSignVO;

import java.util.List;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Repository
public interface StudentSignMapper extends BaseMapper<StudentSignDO> {

    /**
     * 根据signId查询签到了该项目的学生
     * @param signId 签到项目id
     * @param pager 分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectUserSignDetailBySignId(Page pager, Long signId);

    /**
     * 根据signId查询迟到签到了该项目的学生
     * @param signId 签到项目id
     * @param pager 分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectLateUserSignDetailBySignId(Page pager, Long signId);

    /**
     * 根据signId查询作废签到了该项目的学生
     * @param signId 签到项目id
     * @param pager 分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectCancelUserSignDetailBySignId(Page pager, Long signId);

    /**
     * 根据signId查询未签到该项目的学生
     * @param signId 签到项目id
     * @param pager 分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectUnsignedUserDetailBySignId(Page pager, Long signId);

    /**
     * 根据signId查询该签到项目的学生
     * @param signId 签到项目id
     * @param pager 分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectClassUserSignDetailBySignId(Page pager, Long signId);
}
