package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.WorkDO;
import vip.gadfly.chakkispring.vo.WorkCountVO;
import vip.gadfly.chakkispring.vo.WorkVO;

import java.util.List;

/**
 * <p>
 * 作业项目表 Mapper 接口
 * </p>
 *
 * @author Gadfly
 * @since 2021-02-08
 */
@Repository
public interface WorkMapper extends BaseMapper<WorkDO> {

    IPage<WorkVO> selectWorkPageByClassId(Page pager, @Param("classId") Integer classId);

    List<String> selectFileExtendByWorkId(@Param("workId") Integer workId);

    IPage<WorkVO> selectWorkPageForStudentByClassId(Page pager, @Param("userId") Integer userId, @Param("classId") Integer classId);

    WorkVO selectWorkForStudent(@Param("userId") Integer userId, @Param("workId") Integer id);

    Long selectFileSizeById(@Param("workId") Integer workId);

    WorkCountVO selectWorkCountInfoById(@Param("workId") Integer workId);
}
