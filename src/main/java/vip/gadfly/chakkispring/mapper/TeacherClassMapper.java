package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.TeacherClassDO;
import vip.gadfly.chakkispring.vo.TeacherClassVO;

import java.util.List;

/**
 * @author Gadfly
 */
@Repository
public interface TeacherClassMapper extends BaseMapper<TeacherClassDO> {
    IPage<TeacherClassVO> selectTeacherDetailByClassId(Page pager, Integer classId);

    int insertBatch(@Param("relations") List<TeacherClassDO> relations);
}
