package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.StudentSignDO;
import vip.gadfly.chakkispring.vo.StudentSignVO;

/**
 * @author Gadfly
 */
@Repository
public interface StudentSignMapper extends BaseMapper<StudentSignDO> {

    /**
     * 根据signId查询签到了该项目的学生
     *
     * @param signId 签到项目id
     * @param pager  分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectUserSignDetailBySignId(Page pager, @Param("signId") Integer signId,
                                                      @Param("username") String username,
                                                      @Param("orderByIP") boolean orderByIP);

    /**
     * 根据signId查询迟到签到了该项目的学生
     *
     * @param signId 签到项目id
     * @param pager  分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectLateUserSignDetailBySignId(Page pager, @Param("signId") Integer signId,
                                                          @Param("username") String username,
                                                          @Param("orderByIP") boolean orderByIP);

    /**
     * 根据signId查询作废签到了该项目的学生
     *
     * @param signId 签到项目id
     * @param pager  分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectCancelUserSignDetailBySignId(Page pager, @Param("signId") Integer signId,
                                                            @Param("username") String username,
                                                            @Param("orderByIP") boolean orderByIP);

    /**
     * 根据signId查询未签到该项目的学生
     *
     * @param signId 签到项目id
     * @param pager  分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectUnsignedUserDetailBySignId(Page pager, @Param("signId") Integer signId,
                                                          @Param("username") String username);

    /**
     * 根据signId查询该签到项目的学生
     *
     * @param signId 签到项目id
     * @param pager  分页
     * @return 签到用户列表
     */
    IPage<StudentSignVO> selectClassUserSignDetailBySignId(Page pager, @Param("signId") Integer signId,
                                                           @Param("username") String username,
                                                           @Param("orderByIP") boolean orderByIP);

    long countClassUserSignDetailBySignId(@Param("signId") Integer signId, @Param("username") String username);
}
