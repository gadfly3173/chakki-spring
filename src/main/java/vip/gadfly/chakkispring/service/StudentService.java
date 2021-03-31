package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import vip.gadfly.chakkispring.dto.lesson.QuestionAnswerDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.vo.QuestionnairePageVO;
import vip.gadfly.chakkispring.vo.SignListVO;

import java.util.List;

/**
 * @author Gadfly
 */

public interface StudentService {

    /**
     * 获得学生班级列表数据
     *
     * @return 分组数据
     */
    List<ClassDO> getStudentClassList();

    /**
     * 学生签到
     *
     * @param signId 签到id
     */
    boolean confirmSign(Integer signId, String ip);

    /**
     * 获得最新签到项目
     *
     * @return 签到项目数据
     */
    SignListVO getLatestSignByClassId(Integer classId);

    boolean signAvailable(Integer signId);

    boolean workAvailable(Integer workId);

    boolean handStudentWork(Integer workId, MultiValueMap<String, MultipartFile> fileMap, String ip);

    IPage<QuestionnairePageVO> getQuestionnairePageForStudentByClassId(Integer classId, Integer count, Integer page);

    void handStudentQuestionnaire(List<QuestionAnswerDTO> dto, Integer id, String ip);
}
