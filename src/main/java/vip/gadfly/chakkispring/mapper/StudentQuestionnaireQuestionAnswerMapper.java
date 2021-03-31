package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.StudentQuestionnaireQuestionAnswerDO;
import vip.gadfly.chakkispring.vo.StudentQuestionnaireAnswerVO;

import java.util.List;

/**
 * @author Gadfly
 */
@Repository
public interface StudentQuestionnaireQuestionAnswerMapper extends BaseMapper<StudentQuestionnaireQuestionAnswerDO> {
    List<StudentQuestionnaireAnswerVO> selectAnswersByStudentQuestionnaireId(@Param("studentQuestionnaireId") Integer studentQuestionnaireId);

    List<Integer> selectOptionsByStudentQuestionnaireId(@Param("questionId") Integer questionId,
                                                        @Param("studentQuestionnaireId") Integer studentQuestionnaireId);
}
