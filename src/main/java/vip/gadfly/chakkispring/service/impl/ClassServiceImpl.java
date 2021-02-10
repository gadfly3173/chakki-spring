package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.gadfly.chakkispring.common.constant.SignStatusConstant;
import vip.gadfly.chakkispring.common.constant.TeacherLevelConstant;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.dto.admin.NewClassDTO;
import vip.gadfly.chakkispring.dto.admin.NewSemesterDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateClassDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateSemesterDTO;
import vip.gadfly.chakkispring.dto.lesson.NewSignDTO;
import vip.gadfly.chakkispring.dto.lesson.NewWorkDTO;
import vip.gadfly.chakkispring.dto.lesson.UpdateSignRecordDTO;
import vip.gadfly.chakkispring.mapper.*;
import vip.gadfly.chakkispring.model.*;
import vip.gadfly.chakkispring.service.ClassManageService;
import vip.gadfly.chakkispring.service.ClassService;
import vip.gadfly.chakkispring.service.UserService;
import vip.gadfly.chakkispring.vo.SignCountVO;
import vip.gadfly.chakkispring.vo.StudentSignVO;
import vip.gadfly.chakkispring.vo.TeacherClassVO;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author gadfly
 */

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, ClassDO> implements ClassService {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassManageService classManageService;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private TeacherClassMapper teacherClassMapper;

    @Autowired
    private SignListMapper signListMapper;

    @Autowired
    private StudentSignMapper studentSignMapper;

    @Autowired
    private SemesterMapper semesterMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private WorkExtendMapper workExtendMapper;

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
    public IPage<SignListDO> getSignPageByClassId(Integer classId, Integer count, Integer page) {
        Page pager = new Page(page, count);
        IPage<SignListDO> iPage;
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
        WorkDO work = WorkDO
                .builder()
                .name(dto.getName())
                .info(dto.getInfo())
                .classId(dto.getClassId())
                .fileNum(dto.getFileNum())
                .fileSize(dto.getFileSize())
                .type(dto.getType())
                .endTime(dto.getEndTime())
                .build();
        System.out.println(work.toString());
        workMapper.insert(work);
        if (dto.getExtendList() != null && dto.getExtendList().size() > 0) {
            for (String extend : dto.getExtendList()) {
                WorkExtendDO workExtendDO = WorkExtendDO
                        .builder()
                        .extend(extend.replaceAll("[^a-zA-Z0-9]", "").trim().toUpperCase())
                        .workId(work.getId())
                        .build();
                workExtendMapper.insert(workExtendDO);
            }
        }
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
}
