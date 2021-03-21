package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.OptionDO;
import vip.gadfly.chakkispring.vo.OptionVO;

import java.util.List;

/**
 * @author Gadfly
 */
@Repository
public interface OptionMapper extends BaseMapper<OptionDO> {
    List<OptionVO> selectOptionsByQuestionId(@Param("questionId") Integer questionId);
}
