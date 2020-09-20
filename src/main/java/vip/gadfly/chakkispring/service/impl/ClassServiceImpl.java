package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.dto.admin.*;
import vip.gadfly.chakkispring.dto.lesson.NewSignDTO;
import vip.gadfly.chakkispring.mapper.ClassMapper;
import vip.gadfly.chakkispring.mapper.SignListMapper;
import vip.gadfly.chakkispring.mapper.StudentClassMapper;
import vip.gadfly.chakkispring.mapper.StudentSignMapper;
import vip.gadfly.chakkispring.model.*;
import vip.gadfly.chakkispring.service.*;
import vip.gadfly.chakkispring.vo.StudentSignVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private SignListMapper signListMapper;

    @Autowired
    private StudentSignMapper studentSignMapper;

    @Autowired
    private ClassMapper classMapper;

    @Override
    public IPage<UserDO> getUserPageByClassId(Long classId, Long count, Long page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        iPage = userService.getUserPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<UserDO> getFreshUserPageByClassId(Long classId, Long count, Long page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        iPage = userService.getFreshUserPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<UserDO> getFreshUserPageByClassIdAndName(Long classId, String name, Long count, Long page) {
        Page pager = new Page(page, count);
        IPage<UserDO> iPage;
        iPage = userService.getFreshUserPageByClassIdAndName(pager, classId, name);
        return iPage;
    }

    @Override
    public List<ClassDO> getUserClassByUserId(Long userId) {
        return this.baseMapper.selectUserClasses(userId);
    }

    @Override
    public IPage<ClassDO> getClassPage(Long page, Long count) {
        return classManageService.getClassPage(page, count);
    }

    @Override
    public ClassDO getClass(Long id) {
        throwClassNotExistById(id);
        return classManageService.getClassById(id);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean createClass(NewClassDTO dto) {
        throwClassNameExist(dto.getName());
        ClassDO lesson = ClassDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        classManageService.save(lesson);
        return true;
    }

    @Override
    public boolean updateClass(Long id, UpdateClassDTO dto) {
        // bug 如果只修改info，不修改name，则name已经存在，此时不应该报错
        ClassDO exist = classManageService.getById(id);
        if (exist == null) {
            throw new NotFoundException("class not found", 10202);
        }
        if (!exist.getName().equals(dto.getName())) {
            throwClassNameExist(dto.getName());
        }
        ClassDO lesson = ClassDO.builder().id(id).name(dto.getName()).info(dto.getInfo()).build();
        return classManageService.updateById(lesson);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteClass(Long id) {
        throwClassNotExistById(id);
        studentClassMapper.removeByClassId(id);
        return classManageService.removeById(id);
    }

    @Override
    public boolean deleteStudentClassRelations(Long userId, List<Long> deleteIds) {
        if (deleteIds == null || deleteIds.isEmpty()) {
            return true;
        }
        QueryWrapper<StudentClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(StudentClassDO::getUserId, userId)
                .in(StudentClassDO::getClassId, deleteIds);
        return studentClassMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean addStudentClassRelations(Long classId, List<Long> addIds) {
        if (addIds == null || addIds.isEmpty()) {
            return true;
        }
        List<StudentClassDO> relations = addIds.stream().map(it -> new StudentClassDO(classId, it)).collect(Collectors.toList());
        return studentClassMapper.insertBatch(relations) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createSign(NewSignDTO validator) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SignListDO sign = new SignListDO();
        sign.setClassId(validator.getClassId());
        sign.setName(validator.getTitle());
//        设置结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, validator.getEndMinutes());
        sign.setEndTime(calendar.getTime());
        ClassDO lesson = classMapper.selectById(validator.getClassId());
        lesson.setSignId(sign.getId());
        return classMapper.updateById(lesson) > 0;
    }


    @Override
    public List<ClassDO> getAllClasses() {
        return classManageService.list();
    }

    @Override
    public IPage<SignListDO> getSignPageByClassId(Long classId, Long count, Long page) {
        Page pager = new Page(page, count);
        IPage<SignListDO> iPage;
        iPage = signListMapper.selectSignPageByClassId(pager, classId);
        return iPage;
    }

    @Override
    public IPage<StudentSignVO> getUserPageBySignId(Long signId, Boolean signStatus, Long count, Long page) {
        Page pager = new Page(page, count);
        IPage<StudentSignVO> iPage;
        if (signStatus) {
            iPage = studentSignMapper.selectUserSignDetailBySignId(pager, signId);
        } else {
            iPage = studentSignMapper.selectUnsignedUserDetailBySignId(pager, signId);
        }
        return iPage;
    }

    private void throwClassNotExistById(Long id) {
        boolean exist = classManageService.checkClassExistById(id);
        if (!exist) {
            throw new NotFoundException("class not found", 10202);
        }
    }

    private void throwClassNameExist(String name) {
        boolean exist = classManageService.checkClassExistByName(name);
        if (exist) {
            throw new ForbiddenException("class name already exist, please enter a new one", 10203);
        }
    }
}
