package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.QuestionnaireQuestionDO;
import vip.gadfly.chakkispring.vo.QuestionVO;

import java.util.List;

/**
 * @author Gadfly
 */
@Repository
public interface QuestionnaireQuestionMapper extends BaseMapper<QuestionnaireQuestionDO> {
    List<QuestionVO> selectQuestionsByQuestionnaireId(@Param("questionnaireId") Integer questionnaireId);
}
