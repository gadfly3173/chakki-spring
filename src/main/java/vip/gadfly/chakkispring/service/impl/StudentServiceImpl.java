package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.FailedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.constant.SignStatusConstant;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.common.util.IPUtil;
import vip.gadfly.chakkispring.mapper.*;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.FileDO;
import vip.gadfly.chakkispring.model.StudentSignDO;
import vip.gadfly.chakkispring.model.StudentWorkDO;
import vip.gadfly.chakkispring.module.file.Uploader;
import vip.gadfly.chakkispring.service.FileService;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.QuestionnairePageVO;
import vip.gadfly.chakkispring.vo.SignListVO;

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
        IPage<QuestionnairePageVO> iPage;
        iPage = questionnaireMapper.selectQuestionnairePageForStudentByClassId(pager, classId, userId);
        return iPage;
    }

    @Override
    public SignListVO getLatestSignByClassId(Integer classId) {
        Integer userId = LocalUser.getLocalUser().getId();
        return signListMapper.getStudentLatestSignByClassId(classId, userId);
    }
}
