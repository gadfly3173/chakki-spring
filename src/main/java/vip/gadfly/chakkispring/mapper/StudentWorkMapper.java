package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.StudentWorkDO;
import vip.gadfly.chakkispring.vo.StudentWorkVO;

/**
 * @author Gadfly
 */
@Repository
public interface StudentWorkMapper extends BaseMapper<StudentWorkDO> {
    IPage<StudentWorkVO> selectUserWorkDetailByWorkId(@Param("pager") Page pager, @Param("workId") Integer workId, @Param("username") String username, @Param("orderByIP") boolean orderByIP);

    IPage<StudentWorkVO> selectUnhandedUserWorkDetailByWorkId(@Param("pager") Page pager, @Param("workId") Integer workId, @Param("username") String username, @Param("orderByIP") boolean orderByIP);

    long countClassUserWorkDetailByWorkId(@Param("workId") Integer workId, @Param("username") String username);

    IPage<StudentWorkVO> selectClassUserWorkDetailByWorkId(@Param("pager") Page pager, @Param("workId") Integer workId, @Param("username") String username, @Param("orderByIP") boolean orderByIP);
}
