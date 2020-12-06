package vip.gadfly.chakkispring.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.mapper.SignListMapper;
import vip.gadfly.chakkispring.mapper.StudentClassMapper;
import vip.gadfly.chakkispring.mapper.TeacherClassMapper;
import vip.gadfly.chakkispring.model.StudentClassDO;
import vip.gadfly.chakkispring.model.TeacherClassDO;
import vip.gadfly.chakkispring.model.UserDO;

import javax.annotation.PostConstruct;


/**
 * 班级用户关联查询工具
 */
@SuppressWarnings("unchecked")
@Component
@Slf4j
public class ClassPermissionCheckUtil {

    private static ClassPermissionCheckUtil classPermissionCheckUtil;
    @Autowired
    private StudentClassMapper studentClassMapper;
    @Autowired
    private TeacherClassMapper teacherClassMapper;
    @Autowired
    private SignListMapper signListMapper;

    public static boolean isStudentInClassByClassId(Long classId) {
        return isStudentInClass(classId);
    }

    public static boolean isStudentInClassBySignId(Long signId) {
        Long classId = classPermissionCheckUtil.signListMapper.selectById(signId).getClassId();
        return isStudentInClass(classId);
    }

    public static boolean isTeacherInClassByClassId(Long classId) {
        return isTeacherInClass(classId);
    }

    public static boolean isTeacherInClassBySignId(Long signId) {
        Long classId = classPermissionCheckUtil.signListMapper.selectById(signId).getClassId();
        return isTeacherInClass(classId);
    }

    private static boolean isStudentInClass(Long classId) {
        UserDO user = LocalUser.getLocalUser();
        Long userId = user.getId();
        QueryWrapper<StudentClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StudentClassDO::getUserId, userId).eq(StudentClassDO::getClassId, classId);
        boolean result = classPermissionCheckUtil.studentClassMapper.selectCount(wrapper) > 0;
        if (!result) {
            log.warn("学生用户：" + userId + "非法访问班级id：" + classId);
        }
        return result;
    }

    private static boolean isTeacherInClass(Long classId) {
        UserDO user = LocalUser.getLocalUser();
        Long userId = user.getId();
        QueryWrapper<TeacherClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TeacherClassDO::getUserId, userId).eq(TeacherClassDO::getClassId, classId);
        boolean result = classPermissionCheckUtil.teacherClassMapper.selectCount(wrapper) > 0;
        if (!result) {
            log.warn("教师用户：" + userId + "非法访问班级id：" + classId);
        }
        return result;
    }

    @PostConstruct
    public void init() {
        classPermissionCheckUtil = this;
        classPermissionCheckUtil.studentClassMapper = this.studentClassMapper;
        classPermissionCheckUtil.teacherClassMapper = this.teacherClassMapper;
        classPermissionCheckUtil.signListMapper = this.signListMapper;
    }
}
