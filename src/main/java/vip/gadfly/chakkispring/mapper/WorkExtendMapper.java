package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.WorkExtendDO;

import java.util.List;

/**
 * <p>
 * 作业-扩展名关系表 Mapper 接口
 * </p>
 *
 * @author Gadfly
 * @since 2021-02-08
 */
@Repository
public interface WorkExtendMapper extends BaseMapper<WorkExtendDO> {

    void insertBatch(@Param("relations") List<WorkExtendDO> relations);
}