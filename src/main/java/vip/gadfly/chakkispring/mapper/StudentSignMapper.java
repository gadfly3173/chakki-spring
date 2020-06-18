package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.StudentSignDO;

import java.util.List;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Repository
public interface StudentSignMapper extends BaseMapper<StudentSignDO> {

    int insertBatch(@Param("relations") List<StudentSignDO> relations);

}
