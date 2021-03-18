package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.QuestionnaireDO;
import vip.gadfly.chakkispring.vo.QuestionnaireVO;

/**
 * @author Gadfly
 */
@Repository
public interface QuestionnaireMapper extends BaseMapper<QuestionnaireDO> {

    IPage<QuestionnaireVO> selectQuestionnairePageByClassId(Page pager, @Param("classId") Integer classId);

}
