package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.FailedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.constant.QuestionTypeConstant;
import vip.gadfly.chakkispring.common.constant.SignStatusConstant;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.common.util.IPUtil;
import vip.gadfly.chakkispring.dto.lesson.QuestionAnswerDTO;
import vip.gadfly.chakkispring.mapper.*;
import vip.gadfly.chakkispring.model.*;
import vip.gadfly.chakkispring.module.file.Uploader;
import vip.gadfly.chakkispring.service.FileService;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Gadfly
 */

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private SignListMapper signListMapper;

    @Autowired
    private StudentSignMapper studentSignMapper;

    @Autowired
    private StudentWorkMapper studentWorkMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private WorkExtensionMapper workExtensionMapper;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private StudentQuestionnaireMapper studentQuestionnaireMapper;

    @Autowired
    private StudentQuestionnaireQuestionAnswerMapper studentQuestionnaireQuestionAnswerMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private Uploader uploader;

    @Autowired
    private FileService fileService;

    @Override
    public List<ClassDO> getStudentClassList() {
        Integer userId = LocalUser.getLocalUser().getId();
        return classMapper.selectUserClasses(userId);
    }

    @Override
    public boolean confirmSign(Integer signId, String ip) {
        Integer userId = LocalUser.getLocalUser().getId();
        QueryWrapper<StudentSignDO> wrapper = new QueryWrapper<>();
        if (IPUtil.isInternalIp(ip)) {
            wrapper.lambda()
                    .eq(StudentSignDO::getSignId, signId)
                    .and(i -> i.eq(StudentSignDO::getUserId, userId)
                            .or()
                            .eq(StudentSignDO::getIp, ip));
        } else {
            wrapper.lambda().eq(StudentSignDO::getSignId, signId).eq(StudentSignDO::getUserId, userId);
        }
        if (studentSignMapper.selectCount(wrapper) == 0) {
            return studentSignMapper.insert(new StudentSignDO(signId, userId, ip,
                    SignStatusConstant.STATUS_SIGNED)) > 0;
        }
        return false;
    }

    @Override
    public boolean signAvailable(Integer signId) {
        return signListMapper.selectById(signId).getEndTime().getTime() > System.currentTimeMillis();
    }

    @Override
    public boolean workAvailable(Integer workId) {
        Date workEndTime = workMapper.selectById(workId).getEndTime();
        if (workEndTime != null) {
            return workEndTime.getTime() > System.currentTimeMillis();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handStudentWork(Integer workId, MultiValueMap<String, MultipartFile> fileMap, String ip) {
        Integer userId = LocalUser.getLocalUser().getId();
        QueryWrapper<StudentWorkDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StudentWorkDO::getUserId, userId).eq(StudentWorkDO::getWorkId, workId);
        if (studentWorkMapper.selectCount(wrapper) > 0) {
            return false;
        }
        List<String> include = workExtensionMapper.selectExtensionList(workId);
        List<Integer> fileIdList = new ArrayList<>();
        Long singleFileLimit = workMapper.selectFileSizeById(workId);
        uploader.upload(fileMap, file -> {
            // 数据库中不存在
            if (!fileService.checkFileExistByMd5(file.getMd5())) {
                FileDO fileDO = new FileDO();
                BeanUtils.copyProperties(file, fileDO);
                fileMapper.insert(fileDO);
                fileIdList.add(fileDO.getId());
                return true;
            }
            // 已存在，则直接抛异常
            throw new FailedException(10232);
        }, include, null, singleFileLimit, 1);
        StudentWorkDO studentWorkDO = StudentWorkDO
                .builder()
                .userId(userId)
                .workId(workId)
                .ip(ip)
                .fileId(fileIdList.get(0))
                .build();
        studentWorkMapper.insert(studentWorkDO);
        return true;
    }

    @Override
    public IPage<QuestionnairePageVO> getQuestionnairePageForStudentByClassId(Integer classId, Integer count, Integer page) {
        Integer userId = LocalUser.getLocalUser().getId();
        Page pager = new Page(page, count);
        pager.setSearchCount(false);
        pager.setTotal(questionnaireMapper.selectCountByClassId(classId));
        IPage<QuestionnairePageVO> iPage;
        iPage = questionnaireMapper.selectQuestionnairePageForStudentByClassId(pager, userId, classId);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handStudentQuestionnaire(List<QuestionAnswerDTO> dto, Integer id, String ip) {
        QuestionnaireVO questionnaireVO = questionnaireMapper.getQuestionnaireVO(id);
        // 回答与题目数量对不上就抛异常
        if (questionnaireVO.getQuestions().size() != dto.size()) {
            throw new FailedException(10250);
        }
        // 插入学生提交问卷记录
        StudentQuestionnaireDO studentQuestionnaireDO = StudentQuestionnaireDO.builder()
                .questionnaireId(id)
                .userId(LocalUser.getLocalUser().getId())
                .ip(ip)
                .build();
        studentQuestionnaireMapper.insert(studentQuestionnaireDO);
        // 遍历验证每个问题是否符合回答要求并插入
        for (int i = 0; i < dto.size(); i++) {
            QuestionVO questionVO = questionnaireVO.getQuestions().get(i);
            QuestionAnswerDTO questionAnswerDTO = dto.get(i);
            // 简答题逻辑
            if (questionVO.getType() == QuestionTypeConstant.TEXT) {
                // 回答内容空白
                if (!StringUtils.hasText(questionAnswerDTO.getAnswer())) {
                    throw new FailedException(10250, String.format("第%d题的回答不得为空", i + 1));
                }
                // 插入回答记录
                StudentQuestionnaireQuestionAnswerDO answerDO =
                        StudentQuestionnaireQuestionAnswerDO.builder()
                                .questionId(questionVO.getId())
                                .studentQuestionId(studentQuestionnaireDO.getId())
                                .answer(questionAnswerDTO.getAnswer())
                                .build();
                studentQuestionnaireQuestionAnswerMapper.insert(answerDO);
                continue;
            }
            // 选择题逻辑
            if (questionVO.getType() == QuestionTypeConstant.SELECT) {
                // 单选
                if (questionVO.getLimitMax() == 1) {
                    // 选项为空
                    if (questionAnswerDTO.getSingleOptionId() == null) {
                        throw new FailedException(10250, String.format("第%d题的选项不得为空", i + 1));
                    }
                    // 判断选项是否是问题给出的
                    int index = 0;
                    for (OptionVO option : questionVO.getOptions()) {
                        // 存在就插入
                        if (option.getId().equals(questionAnswerDTO.getSingleOptionId())) {
                            StudentQuestionnaireQuestionAnswerDO answerDO =
                                    StudentQuestionnaireQuestionAnswerDO.builder()
                                            .studentQuestionId(studentQuestionnaireDO.getId())
                                            .questionId(questionVO.getId())
                                            .optionId(option.getId())
                                            .build();
                            studentQuestionnaireQuestionAnswerMapper.insert(answerDO);
                            index += 1;
                        }
                    }
                    // 不存在就抛异常
                    if (index == 0) {
                        throw new FailedException(10250, String.format("第%d题的选项不在问题中", i + 1));
                    }
                    continue;
                }
                // 多选
                if (questionVO.getLimitMax() > 1) {
                    // 选项为空
                    if (questionAnswerDTO.getMultiOptionId() == null || questionAnswerDTO.getMultiOptionId().isEmpty()) {
                        throw new FailedException(10250, String.format("第%d题的选项不得为空", i + 1));
                    }
                    // 选项超过设置的上限
                    if (questionAnswerDTO.getMultiOptionId().size() > questionVO.getLimitMax()) {
                        throw new FailedException(10250, String.format("第%d题的选项超出上限", i + 1));
                    }
                    // 判断选项是否是问题给出的
                    int index = 0;
                    // 先遍历问题里的选项
                    for (OptionVO option : questionVO.getOptions()) {
                        // 再遍历提交的选项
                        for (Integer selectId : questionAnswerDTO.getMultiOptionId()) {
                            // 存在就插入
                            if (selectId.equals(option.getId())) {
                                StudentQuestionnaireQuestionAnswerDO answerDO =
                                        StudentQuestionnaireQuestionAnswerDO.builder()
                                                .studentQuestionId(studentQuestionnaireDO.getId())
                                                .questionId(questionVO.getId())
                                                .optionId(option.getId())
                                                .build();
                                studentQuestionnaireQuestionAnswerMapper.insert(answerDO);
                                index += 1;
                            }
                        }
                    }
                    // 插入成功数量少于提交的选项就抛异常
                    if (index < questionAnswerDTO.getMultiOptionId().size()) {
                        throw new FailedException(10250, String.format("第%d题的选项不在问题中", i + 1));
                    }
                }
            }
        }
    }

    @Override
    public SignListVO getLatestSignByClassId(Integer classId) {
        Integer userId = LocalUser.getLocalUser().getId();
        return signListMapper.getStudentLatestSignByClassId(classId, userId);
    }
}
