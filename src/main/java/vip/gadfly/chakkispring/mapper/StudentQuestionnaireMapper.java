package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.StudentQuestionnaireDO;
import vip.gadfly.chakkispring.vo.StudentQuestionnaireAnswerVO;

import java.util.List;

/**
 * @author Gadfly
 */
@Repository
public interface StudentQuestionnaireMapper extends BaseMapper<StudentQuestionnaireDO> {
    List<StudentQuestionnaireAnswerVO> selectStudentQuestionnaireAnswerVO(@Param("id") Integer id);
}
