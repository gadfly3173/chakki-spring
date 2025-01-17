package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.mapper.ClassMapper;
import vip.gadfly.chakkispring.mapper.StudentClassMapper;
import vip.gadfly.chakkispring.mapper.TeacherClassMapper;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.StudentClassDO;
import vip.gadfly.chakkispring.model.TeacherClassDO;
import vip.gadfly.chakkispring.service.ClassManageService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gadfly
 */
@Service
public class ClassManageServiceImpl extends ServiceImpl<ClassMapper, ClassDO> implements ClassManageService {
    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private TeacherClassMapper teacherClassMapper;

    @Override
    public List<ClassDO> getUserClassesByUserId(Integer userId) {
        return this.baseMapper.selectUserClasses(userId);
    }

    @Override
    public List<Integer> getUserClassIdsByUserId(Integer userId) {
        return this.baseMapper.selectUserClassIds(userId);
    }

    @Override
    public IPage<ClassDO> getClassPage(Integer page, Integer count) {
        Page<ClassDO> pager = new Page<>(page, count);
        return this.baseMapper.selectPage(pager, null);
    }

    @Override
    public ClassDO getClassById(Integer id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public boolean checkClassExistById(Integer id) {
        return this.baseMapper.selectCountById(id) > 0;
    }

    @Override
    public boolean checkClassExistByName(String name) {
        QueryWrapper<ClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ClassDO::getName, name);
        return this.baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean deleteUserClassRelations(Integer userId, List<Integer> deleteIds) {
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
    public List<Integer> getClassUserIds(Integer id) {
        QueryWrapper<StudentClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StudentClassDO::getClassId, id);
        List<StudentClassDO> list = studentClassMapper.selectList(wrapper);
        return list.stream().map(StudentClassDO::getUserId).collect(Collectors.toList());
    }

    @Override
    public boolean deleteTeacherClassRelations(Integer userId, List<Integer> classIds) {
        if (classIds == null || classIds.isEmpty()) {
            return false;
        }
        QueryWrapper<TeacherClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(TeacherClassDO::getUserId, userId)
                .in(TeacherClassDO::getClassId, classIds);
        return teacherClassMapper.delete(wrapper) > 0;
    }

    @Override
    public List<ClassDO> getClassesBySemesterAndTeacher(Integer semesterId, Integer teacherId) {
        return this.baseMapper.selectClassesBySemesterAndTeacher(semesterId, teacherId);
    }

    @Override
    public List<ClassDO> getClassesBySemesterAndStudent(Integer semesterId, Integer userId) {
        return this.baseMapper.selectClassesBySemesterAndStudent(semesterId, userId);
    }

    private boolean checkClassExistByIds(List<Integer> ids) {
        return ids.stream().allMatch(this::checkClassExistById);
    }
}
