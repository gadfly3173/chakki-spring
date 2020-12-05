package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.StudentClassDO;

import java.util.List;

/**
 * @author Gadfly
 */
@Repository
public interface StudentClassMapper extends BaseMapper<StudentClassDO> {

    int insertBatch(@Param("relations") List<StudentClassDO> relations);

    int removeByClassId(@Param("class_id") Long class_id);
}
