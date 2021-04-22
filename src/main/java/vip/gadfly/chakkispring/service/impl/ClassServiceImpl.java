package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import vip.gadfly.chakkispring.bo.FileExportBO;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.constant.QuestionTypeConstant;
import vip.gadfly.chakkispring.common.constant.SignStatusConstant;
import vip.gadfly.chakkispring.common.constant.TeacherLevelConstant;
import vip.gadfly.chakkispring.common.constant.WorkStatusConstant;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.common.util.ZipUtil;
import vip.gadfly.chakkispring.dto.admin.NewClassDTO;
import vip.gadfly.chakkispring.dto.admin.NewSemesterDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateClassDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateSemesterDTO;
import vip.gadfly.chakkispring.dto.lesson.*;
import vip.gadfly.chakkispring.mapper.*;
import vip.gadfly.chakkispring.model.*;
import vip.gadfly.chakkispring.module.file.FileProperties;
import vip.gadfly.chakkispring.module.file.FileUtil;
import vip.gadfly.chakkispring.module.file.Uploader;
import vip.gadfly.chakkispring.service.ClassManageService;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.UserService;
import vip.gadfly.chakkispring.vo.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;


/**
 * @author gadfly
 */

@SuppressWarnings("unchecked")
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, ClassDO> implements ClassService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassManageService classManageService;

    @Autowired
    private Uploader uploader;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private TeacherClassMapper teacherClassMapper;

    @Autowired
    private SignListMapper signListMapper;

    @Autowired
    private StudentSignMapper studentSignMapper;

    @Autowired
    private StudentWorkMapper studentWorkMapper;

    @Autowired
    private SemesterMapper semesterMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private WorkExtensionMapper workExtendMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private QuestionnaireQuestionMapper questionnaireQuestionMapper;

    @Autowired
    private QuestionnaireQuestionOptionMapper questionnaireQuestionOptionMapper;

    @Autowired
    private StudentQuestionnaireMapper studentQuestionnaireMapper;

    @Autowired
    private FileProperties fileProperties;

    @Override
    public IPage<UserDO> getUserPageByClassId(Integer classId, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        iPage = userService.getUserPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<UserDO> getFreshUserPageByClassId(Integer classId, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        iPage = userService.getFreshUserPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<UserDO> getFreshUserPageByClassIdAndName(Integer classId, String name, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        iPage = userService.getFreshUserPageByClassIdAndName(pager, classId, name);
        return iPage;
    }

    @Override
    public List<ClassDO> getUserClassByUserId(Integer userId) {
        return this.baseMapper.selectUserClasses(userId);
    }

    @Override
    public IPage<ClassDO> getClassPage(Integer page, Integer count) {
        return classManageService.getClassPage(page, count);
    }

    @Override
    public ClassDO getClass(Integer id) {
        throwClassNotExistById(id);
        return classManageService.getClassById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createClass(NewClassDTO dto) {
        throwClassNameExist(dto.getName());
        throwSemesterNotExistById(dto.getSemesterId());
        ClassDO lesson = ClassDO.builder()
                .name(dto.getName())
                .info(dto.getInfo())
                .semesterId(dto.getSemesterId())
                .build();
        classManageService.save(lesson);
        return true;
    }

    @Override
    public boolean updateClass(Integer id, UpdateClassDTO dto) {
        ClassDO exist = classManageService.getById(id);
        if (exist == null) {
            throw new NotFoundException(10202);
        }
        if (!exist.getName().equals(dto.getName())) {
            throwClassNameExist(dto.getName());
        }
        throwSemesterNotExistById(dto.getSemesterId());
        ClassDO lesson = ClassDO.builder()
                .name(dto.getName())
                .info(dto.getInfo())
                .semesterId(dto.getSemesterId())
                .build();
        lesson.setId(id);
        return classManageService.updateById(lesson);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteClass(Integer id) {
        throwClassNotExistById(id);
        studentClassMapper.removeByClassId(id);
        return classManageService.removeById(id);
    }

    @Override
    public boolean deleteStudentClassRelations(Integer userId, List<Integer> deleteIds) {
        return classManageService.deleteUserClassRelations(userId, deleteIds);
    }

    @Override
    public boolean addStudentClassRelations(Integer classId, List<Integer> addIds) {
        if (addIds == null || addIds.isEmpty()) {
            return false;
        }
        throwClassNotExistById(classId);
        List<StudentClassDO> relations =
                addIds.stream().map(it -> new StudentClassDO(classId, it)).collect(Collectors.toList());
        return studentClassMapper.insertBatch(relations) > 0;
    }

    @Override
    public void createSign(NewSignDTO validator) {
        SignListDO sign = new SignListDO();
        sign.setClassId(validator.getClassId());
        sign.setName(validator.getTitle());
        // 设置结束时间
        Calendar calendar = Calendar.getInstance();
        sign.setCreateTime(calendar.getTime());
        calendar.add(Calendar.MINUTE, validator.getEndMinutes());
        sign.setEndTime(calendar.getTime());
        signListMapper.insert(sign);
    }


    @Override
    public List<ClassDO> getAllClasses() {
        QueryWrapper<ClassDO> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return classManageService.list(wrapper);
    }

    @Override
    public IPage<SignListVO> getSignPageByClassId(Integer classId, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<SignListVO> iPage;
        iPage = signListMapper.selectSignPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<StudentSignVO> getUserPageBySignId(Integer signId, Integer signStatus, String username, Integer count,
                                                    Integer page, boolean orderByIP) {
        Page pager = new Page(page, count);
        IPage<StudentSignVO> iPage;
        switch (signStatus) {
            case SignStatusConstant.STATUS_SIGNED:
                iPage = studentSignMapper.selectUserSignDetailBySignId(pager, signId, username, orderByIP);
                break;
            case SignStatusConstant.STATUS_LATE:
                iPage = studentSignMapper.selectLateUserSignDetailBySignId(pager, signId, username, orderByIP);
                break;
            case SignStatusConstant.STATUS_CANCEL:
                iPage = studentSignMapper.selectUnsignedUserDetailBySignId(pager, signId, username);
                break;
            case 0:
            default:
                pager.setSearchCount(false);
                pager.setTotal(studentSignMapper.countClassUserSignDetailBySignId(signId, username));
                iPage = studentSignMapper.selectClassUserSignDetailBySignId(pager, signId, username, orderByIP);
        }
        return iPage;
    }

    @Override
    public SignCountVO getSign(Integer id) {
        return signListMapper.selectSignCountInfoById(id);
    }

    @Override
    public boolean updateSignRecord(UpdateSignRecordDTO validator, Integer signId) {
        QueryWrapper<StudentSignDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StudentSignDO::getUserId, validator.getUserId()).eq(StudentSignDO::getSignId, signId);
        StudentSignDO studentSignDO = studentSignMapper.selectOne(wrapper);
        if (studentSignDO == null) {
            return studentSignMapper.insert(new StudentSignDO(signId, validator.getUserId(), "教师代签",
                    validator.getSignStatus())) > 0;
        }
        studentSignDO.setStatus(validator.getSignStatus());
        return studentSignMapper.updateById(studentSignDO) > 0;
    }

    @Override
    public IPage<TeacherClassVO> getTeacherPageByClassId(Integer classId) {
        Page pager = new Page(0, 10);
        IPage<TeacherClassVO> iPage;
        iPage = teacherClassMapper.selectTeacherDetailByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<UserDO> getFreshTeacherPageByClassIdAndName(Integer classId, String name, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        iPage = userService.getFreshTeacherPageByClassIdAndName(pager, classId, name);
        return iPage;
    }

    @Override
    public boolean deleteTeacherClassRelations(Integer userId, List<Integer> classIds) {
        return classManageService.deleteTeacherClassRelations(userId, classIds);
    }

    @Override
    public boolean addTeacherClassRelations(Integer classId, List<Integer> userIds, Integer level) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        // 主教师限制一人
        if (level == TeacherLevelConstant.MAIN_LEVEL) {
            QueryWrapper<TeacherClassDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(TeacherClassDO::getClassId, classId).eq(TeacherClassDO::getLevel, level);
            int count = teacherClassMapper.selectCount(wrapper);
            if (count > 0) {
                return false;
            }
        }
        List<TeacherClassDO> relations =
                userIds.stream().map(it -> new TeacherClassDO(classId, it, level)).collect(Collectors.toList());
        return teacherClassMapper.insertBatch(relations) > 0;
    }

    @Override
    public boolean createSemester(NewSemesterDTO validator) {
        throwSemesterNameExist(validator.getName());
        return semesterMapper.insert(
                SemesterDO.builder()
                        .name(validator.getName())
                        .info(validator.getInfo())
                        .build()) > 0;
    }

    @Override
    public List<SemesterDO> getAllSemesters() {
        QueryWrapper<SemesterDO> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return semesterMapper.selectList(wrapper);
    }

    @Override
    public boolean updateSemester(Integer id, UpdateSemesterDTO dto) {
        SemesterDO exist = semesterMapper.selectById(id);
        if (exist == null) {
            throw new NotFoundException(10220);
        }
        if (!exist.getName().equals(dto.getName())) {
            throwSemesterNameExist(dto.getName());
        }
        SemesterDO semester = SemesterDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        semester.setId(id);
        return semesterMapper.updateById(semester) > 0;
    }

    @Override
    public boolean deleteSemester(Integer id) {
        return semesterMapper.deleteById(id) > 0;
    }

    @Override
    public List<ClassDO> getClassesBySemesterAndTeacher(Integer semesterId, Integer teacherId) {
        return classManageService.getClassesBySemesterAndTeacher(semesterId, teacherId);
    }

    @Override
    public List<ClassDO> getClassesBySemesterAndStudent(Integer semesterId, Integer userId) {
        return classManageService.getClassesBySemesterAndStudent(semesterId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWork(NewWorkDTO dto) {
        // 扩展名列表只保留字母并大写，生成List<WorkExtensionDO>
        TreeSet<String> treeSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        treeSet.addAll(dto.getFileExtension());
        WorkDO work = WorkDO
                .builder()
                .name(dto.getName())
                .info(dto.getInfo())
                .classId(dto.getClassId())
                .fileSize(dto.getFileSize())
                .type(dto.getType())
                .endTime(dto.getEndTime())
                .build();
        workMapper.insert(work);
        if (treeSet.size() > 0) {
            // 扩展名列表只保留字母并大写，生成List<WorkExtensionDO>
            List<WorkExtensionDO> relations = treeSet
                    .stream()
                    .map(ext -> WorkExtensionDO.builder()
                            .workId(work.getId())
                            .extension(ext
                                    .replaceAll("[^a-zA-Z0-9]", "")
                                    .toUpperCase())
                            .build())
                    .collect(Collectors.toList());
            workExtendMapper.insertBatch(relations);
        }
    }

    @Override
    public IPage<WorkVO> getWorkPageByClassId(Integer classId, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<WorkVO> iPage;
        iPage = workMapper.selectWorkPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<WorkVO> getWorkPageForStudentByClassId(Integer classId, Integer count, Integer page) {
        Integer userId = LocalUser.getLocalUser().getId();
        Page pager = new Page(page, count);
        IPage<WorkVO> iPage;
        iPage = workMapper.selectWorkPageForStudentByClassId(pager, userId, classId);
        return iPage;
    }

    @Override
    public WorkVO getOneWorkForStudent(Integer id) {
        Integer userId = LocalUser.getLocalUser().getId();
        return workMapper.selectWorkForStudent(userId, id);
    }

    @Override
    public IPage<StudentWorkVO> getUserPageByWorkId(Integer workId, Integer workStatus, String username, Integer count, Integer page, boolean orderByIP) {
        Page pager = new Page(page, count);
        IPage<StudentWorkVO> iPage;
        switch (workStatus) {
            case WorkStatusConstant.HANDED:
                iPage = studentWorkMapper.selectUserWorkDetailByWorkId(pager, workId, username, orderByIP);
                break;
            case WorkStatusConstant.UNHANDED:
                iPage = studentWorkMapper.selectUnhandedUserWorkDetailByWorkId(pager, workId, username, orderByIP);
                break;
            case WorkStatusConstant.ALL:
            default:
                pager.setSearchCount(false);
                pager.setTotal(studentWorkMapper.countClassUserWorkDetailByWorkId(workId, username));
                iPage = studentWorkMapper.selectClassUserWorkDetailByWorkId(pager, workId, username, orderByIP);
        }
        return iPage;
    }

    @Override
    public WorkCountVO getWorkDetail(Integer id) {
        return workMapper.selectWorkCountInfoById(id);
    }

    @Override
    public FileExportBO getStudentWorkFile(Integer id) {
        StudentWorkDO studentWork = studentWorkMapper.selectById(id);
        FileDO fileDO = fileMapper.selectById(studentWork.getFileId());
        String absolutePath = FileUtil.getFileAbsolutePath(fileProperties.getStoreDir(), fileDO.getPath());
        WorkDO work = workMapper.selectById(studentWork.getWorkId());
        UserDO user = userService.getById(studentWork.getUserId());
        File file = new File(absolutePath);
        String filename = String.format("%s_%s_%s_%tF.%s",
                work.getName(),
                user.getUsername(),
                user.getNickname(),
                studentWork.getCreateTime(),
                fileDO.getExtension().toLowerCase());
        return FileExportBO.builder().file(file).filename(filename).build();
    }

    @Override
    public String getStudentWorkFilename(Integer id) {
        StudentWorkDO studentWork = studentWorkMapper.selectById(id);
        WorkDO work = workMapper.selectById(studentWork.getWorkId());
        FileDO fileDO = fileMapper.selectById(studentWork.getFileId());
        UserDO user = userService.getById(studentWork.getUserId());
        return String.format("%s_%s_%s_%tF.%s",
                work.getName(),
                user.getUsername(),
                user.getNickname(),
                studentWork.getCreateTime(),
                fileDO.getExtension().toLowerCase());
    }

    @Override
    public FileExportBO workFilesToZip(Integer id) throws IOException {
        WorkDO work = workMapper.selectById(id);
        QueryWrapper<StudentWorkDO> studentWorkWrapper = new QueryWrapper<>();
        studentWorkWrapper.lambda().eq(StudentWorkDO::getWorkId, work.getId());
        List<StudentWorkDO> studentWorkDOList = studentWorkMapper.selectList(studentWorkWrapper);
        if (studentWorkDOList == null) {
            throw new NotFoundException(10020);
        }
        File zipFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".zip");
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile), StandardCharsets.UTF_8);
            for (StudentWorkDO tempDO : studentWorkDOList) {
                FileDO fileDO = fileMapper.selectById(tempDO.getFileId());
                String absolutePath = FileUtil.getFileAbsolutePath(fileProperties.getStoreDir(), fileDO.getPath());
                File resFile = new File(absolutePath);
                String filename = getStudentWorkFilename(tempDO.getId());
                ZipUtil.zipFile(resFile, "", zos, null, filename);
            }
        } finally {
            if (zos != null) {
                zos.finish();
                zos.close();
            }
        }
        String filename = String.format("%s_%tF.zip",
                work.getName(),
                System.currentTimeMillis());
        return FileExportBO.builder().file(zipFile).filename(filename).build();
    }

    @Override
    public void rateStudentWork(RateStudentWorkDTO dto, Integer id) {
        UpdateWrapper<StudentWorkDO> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(StudentWorkDO::getId, id).set(StudentWorkDO::getRate, dto.getRate());
        studentWorkMapper.update(null, wrapper);
    }

    @Override
    public void deleteStudentWork(Integer id) {
        studentWorkMapper.deleteById(id);
    }

    @Override
    public Integer createAnnouncement(NewAnnouncementDTO dto) {
        // XSS 过滤
        PolicyFactory policy = Sanitizers.FORMATTING
                .and(Sanitizers.LINKS)
                .and(Sanitizers.BLOCKS)
                .and(Sanitizers.IMAGES)
                .and(Sanitizers.STYLES)
                .and(Sanitizers.TABLES);
        String safeHTML = policy.sanitize(dto.getContent());
        AnnouncementDO announcementDO = AnnouncementDO.builder()
                .classId(dto.getClassId())
                .title(dto.getTitle())
                .content(safeHTML)
                .build();
        announcementMapper.insert(announcementDO);
        return announcementDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAnnouncementAttachment(Integer id, MultiValueMap<String, MultipartFile> fileMap) {
        uploader.upload(fileMap, file -> {
            QueryWrapper<FileDO> fileWrapper = new QueryWrapper<>();
            fileWrapper.lambda().eq(FileDO::getMd5, file.getMd5());
            FileDO found = fileMapper.selectOne(fileWrapper);
            // 数据库中不存在
            if (found == null) {
                FileDO fileDO = new FileDO();
                BeanUtils.copyProperties(file, fileDO);
                fileMapper.insert(fileDO);
                updateAnnouncementAttachmentFile(id, file.getKey(), fileDO.getId());
                return true;
            }
            // 已存在
            updateAnnouncementAttachmentFile(id, file.getKey(), found.getId());
            return false;
        }, null, null, FileUtil.parseSize(fileProperties.getSingleLimit()), 1);
        return true;
    }

    @Override
    public IPage<AnnouncementVO> getAnnouncementPageByClassId(Integer classId, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<AnnouncementVO> iPage;
        iPage = announcementMapper.selectAnnouncementPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public AnnouncementVO getAnnouncementVO(Integer id) {
        return announcementMapper.selectAnnouncementVOById(id);
    }

    @Override
    public void deleteAnnouncement(Integer id) {
        announcementMapper.deleteById(id);
    }


    @Override
    public void deleteWork(Integer id) {
        workMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWork(UpdateWorkDTO dto) {
        // 扩展名去重
        TreeSet<String> treeSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        treeSet.addAll(dto.getFileExtension());
        WorkDO work = WorkDO
                .builder()
                .name(dto.getName())
                .info(dto.getInfo())
                .fileSize(dto.getFileSize())
                .type(dto.getType())
                .endTime(dto.getEndTime())
                .build();
        work.setId(dto.getId());
        workMapper.updateById(work);
        if (treeSet.size() > 0) {
            QueryWrapper<WorkExtensionDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(WorkExtensionDO::getWorkId, dto.getId());
            workExtendMapper.delete(wrapper);
            // 扩展名列表只保留字母并大写，生成List<WorkExtensionDO>
            List<WorkExtensionDO> relations = treeSet
                    .stream()
                    .map(ext -> WorkExtensionDO.builder()
                            .workId(work.getId())
                            .extension(ext
                                    .replaceAll("[^a-zA-Z0-9]", "")
                                    .toUpperCase())
                            .build())
                    .collect(Collectors.toList());
            workExtendMapper.insertBatch(relations);
        }
    }

    @Override
    public FileExportBO getAnnouncementFile(Integer id) {
        AnnouncementDO announcementDO = announcementMapper.selectById(id);
        FileDO fileDO = fileMapper.selectById(announcementDO.getFileId());
        String absolutePath = FileUtil.getFileAbsolutePath(fileProperties.getStoreDir(), fileDO.getPath());
        File file = new File(absolutePath);
        String filename = String.format("%s.%s",
                announcementDO.getFilename(),
                fileDO.getExtension().toLowerCase());
        return FileExportBO.builder().file(file).filename(filename).build();
    }

    @Override
    public void updateAnnouncement(Integer id, NewAnnouncementDTO dto) {
        AnnouncementDO announcementDO = announcementMapper.selectById(id);
        announcementDO.setContent(dto.getContent());
        announcementDO.setTitle(dto.getTitle());
        announcementDO.setUpdateTime(null);
        announcementMapper.updateById(announcementDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createQuestionnaire(NewQuestionnaireDTO dto) {
        // 插入问卷获取id
        QuestionnaireDO questionnaireDO = QuestionnaireDO.builder()
                .title(dto.getTitle())
                .info(dto.getInfo())
                .classId(dto.getClassId())
                .endTime(dto.getEndTime())
                .build();
        questionnaireMapper.insert(questionnaireDO);
        // 插入题目
        for (int i = 0; i < dto.getQuestions().size(); i++) {
            NewQuestionDTO questionDTO = dto.getQuestions().get(i);
            QuestionnaireQuestionDO questionnaireQuestionDO = QuestionnaireQuestionDO.builder()
                    .questionnaireId(questionnaireDO.getId())
                    .title(questionDTO.getTitle())
                    .order(i)
                    .type(questionDTO.getType())
                    .limitMax(questionDTO.getLimitMax())
                    .build();
            questionnaireQuestionMapper.insert(questionnaireQuestionDO);
            // 如果是选择题，且选项列表不为空则插入选项
            if (questionDTO.getType() == QuestionTypeConstant.SELECT
                    && questionDTO.getOptions() != null
                    && !questionDTO.getOptions().isEmpty()) {
                for (int k = 0; k < questionDTO.getOptions().size(); k++) {
                    NewOptionDTO optionDTO = questionDTO.getOptions().get(k);
                    QuestionnaireQuestionOptionDO questionnaireQuestionOptionDO = QuestionnaireQuestionOptionDO.builder()
                            .questionId(questionnaireQuestionDO.getId())
                            .title(optionDTO.getTitle())
                            .order(k)
                            .build();
                    questionnaireQuestionOptionMapper.insert(questionnaireQuestionOptionDO);
                }
            }
        }
    }

    @Override
    public IPage<QuestionnairePageVO> getQuestionnairePageByClassId(Integer classId, Integer count, Integer page) {
        Page pager = new Page(page, count);
        pager.setSearchCount(false);
        pager.setTotal(questionnaireMapper.selectCountByClassId(classId));
        IPage<QuestionnairePageVO> iPage;
        iPage = questionnaireMapper.selectQuestionnairePageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public void deleteQuestionnaire(Integer id) {
        questionnaireMapper.deleteById(id);
    }

    @Override
    public QuestionnaireVO getQuestionnaireVO(Integer id) {
        return questionnaireMapper.getQuestionnaireVO(id);
    }

    @Override
    public FileExportBO getQuestionnaireReportFile(Integer id) throws IOException {
        // 查询问卷本体
        QuestionnaireVO questionnaireVO = questionnaireMapper.getQuestionnaireVO(id);
        // 查询学生提交记录
        List<StudentQuestionnaireAnswerVO> answerVOList = studentQuestionnaireMapper.selectStudentQuestionnaireAnswerVO(id);
        // Apache POI 创建 Excel
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet 1");
        // 设置时间格式
        XSSFCreationHelper creationHelper = wb.getCreationHelper();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        // 标题行
        XSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("学号");
        titleRow.createCell(1).setCellValue("姓名");
        // 问题题目作为标题
        for (int i = 0; i < questionnaireVO.getQuestions().size(); i++) {
            titleRow.createCell(i + 2).setCellValue(String.format("第%d题：%s",
                    i + 1,
                    questionnaireVO.getQuestions().get(i).getTitle()));
        }
        titleRow.createCell(questionnaireVO.getQuestions().size() + 2).setCellValue("提交时间");
        // 学生回答内容写入
        for (int i = 0; i < answerVOList.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(answerVOList.get(i).getUsername());
            row.createCell(1).setCellValue(answerVOList.get(i).getNickname());
            // 遍历问题
            for (int j = 0; j < questionnaireVO.getQuestions().size(); j++) {
                // 简答题写入answer
                if (questionnaireVO.getQuestions().get(j).getType().equals(QuestionTypeConstant.TEXT)) {
                    row.createCell(j + 2).setCellValue(answerVOList.get(i).getAnswers().get(j).getAnswer());
                    continue;
                }
                // 选择题查询选项原文写入
                if (questionnaireVO.getQuestions().get(j).getType().equals(QuestionTypeConstant.SELECT)) {
                    // 初始化StringJoiner用来存放原文
                    StringJoiner optionTitles = new StringJoiner(",");
                    for (Integer optionId : answerVOList.get(i).getAnswers().get(j).getOptionId()) {
                        for (OptionVO optionVO : questionnaireVO.getQuestions().get(j).getOptions()) {
                            // 如果id相同就加一条
                            if (optionVO.getId().equals(optionId)) {
                                optionTitles.add(optionVO.getTitle());
                            }
                        }
                    }
                    row.createCell(j + 2).setCellValue(optionTitles.toString());
                }
            }
            // 写入创建时间
            XSSFCell datetimeCell = row.createCell(questionnaireVO.getQuestions().size() + 2);
            datetimeCell.setCellValue(answerVOList.get(i).getCreateTime());
            datetimeCell.setCellStyle(cellStyle);
        }
        // 写入临时文件
        File excelFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".xlsx");
        FileOutputStream outputStream = new FileOutputStream(excelFile);
        wb.write(outputStream);
        outputStream.close();
        // 格式化文件名
        String filename = String.format("问卷调查结果_%s.xlsx", questionnaireVO.getTitle());
        return FileExportBO.builder()
                .file(excelFile)
                .filename(filename)
                .build();
    }

    private void throwSemesterNameExist(String name) {
        QueryWrapper<SemesterDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SemesterDO::getName, name);
        boolean exist = semesterMapper.selectCount(wrapper) > 0;
        if (exist) {
            throw new ForbiddenException(10221);
        }
    }

    private void throwClassNotExistById(Integer id) {
        boolean exist = classManageService.checkClassExistById(id);
        if (!exist) {
            throw new NotFoundException(10202);
        }
    }

    private void throwSemesterNotExistById(Integer id) {
        SemesterDO exist = semesterMapper.selectById(id);
        if (exist == null) {
            throw new NotFoundException(10220);
        }
    }

    private void throwClassNameExist(String name) {
        boolean exist = classManageService.checkClassExistByName(name);
        if (exist) {
            throw new ForbiddenException(10203);
        }
    }

    private void updateAnnouncementAttachmentFile(Integer id, String filename, Integer fileId) {
        AnnouncementDO announcementDO = announcementMapper.selectById(id);
        announcementDO.setFileId(fileId);
        announcementDO.setFilename(filename);
        announcementDO.setUpdateTime(null);
        announcementMapper.updateById(announcementDO);
    }

}
