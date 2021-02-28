package vip.gadfly.chakkispring.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.mapper.*;
import vip.gadfly.chakkispring.model.StudentClassDO;
import vip.gadfly.chakkispring.model.TeacherClassDO;

import javax.annotation.PostConstruct;


/**
 * 班级用户关联查询工具
 */
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

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private StudentWorkMapper studentWorkMapper;

    @Autowired
    private AnnouncementMapper announcementMapper;

    public static boolean isStudentInClassBySignId(Integer signId) {
        Integer classId = getClassIdBySignId(signId);
        return isStudentInClassByClassId(classId);
    }

    public static boolean isStudentInClassByWorkId(Integer workId) {
        Integer classId = getClassIdByWorkId(workId);
        return isStudentInClassByClassId(classId);
    }

    public static boolean isStudentInClassByStudentWorkId(Integer id) {
        Integer workId = getWorkIdByStudentWorkId(id);
        return isStudentInClassByWorkId(workId);
    }

    public static boolean isStudentInClassByAnnouncementId(Integer id) {
        Integer classId = getClassIdByAnnouncementId(id);
        return isStudentInClassByClassId(classId);
    }

    public static boolean isTeacherInClassBySignId(Integer signId) {
        Integer classId = getClassIdBySignId(signId);
        return isTeacherInClassByClassId(classId);
    }

    public static boolean isTeacherInClassByWorkId(Integer workId) {
        Integer classId = getClassIdByWorkId(workId);
        return isTeacherInClassByClassId(classId);
    }

    public static boolean isTeacherInClassByStudentWorkId(Integer id) {
        Integer workId = getWorkIdByStudentWorkId(id);
        return isTeacherInClassByWorkId(workId);
    }

    public static boolean isTeacherInClassByAnnouncementId(Integer id) {
        Integer classId = getClassIdByAnnouncementId(id);
        return isTeacherInClassByClassId(classId);
    }

    public static boolean isStudentInClassByClassId(Integer classId) {
        Integer userId = LocalUser.getLocalUser().getId();
        QueryWrapper<StudentClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StudentClassDO::getUserId, userId).eq(StudentClassDO::getClassId, classId);
        boolean result = classPermissionCheckUtil.studentClassMapper.selectCount(wrapper) > 0;
        if (!result) {
            log.warn("学生用户：" + userId + "非法访问班级id：" + classId);
        }
        return result;
    }

    public static boolean isTeacherInClassByClassId(Integer classId) {
        Integer userId = LocalUser.getLocalUser().getId();
        QueryWrapper<TeacherClassDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TeacherClassDO::getUserId, userId).eq(TeacherClassDO::getClassId, classId);
        boolean result = classPermissionCheckUtil.teacherClassMapper.selectCount(wrapper) > 0;
        if (!result) {
            log.warn("教师用户：" + userId + "非法访问班级id：" + classId);
        }
        return result;
    }

    private static Integer getClassIdBySignId(Integer signId) {
        return classPermissionCheckUtil.signListMapper.selectById(signId).getClassId();
    }

    private static Integer getClassIdByWorkId(Integer workId) {
        return classPermissionCheckUtil.workMapper.selectById(workId).getClassId();
    }

    private static Integer getWorkIdByStudentWorkId(Integer id) {
        return classPermissionCheckUtil.studentWorkMapper.selectById(id).getWorkId();
    }

    private static Integer getClassIdByAnnouncementId(Integer id) {
        return classPermissionCheckUtil.announcementMapper.selectById(id).getClassId();
    }

    @PostConstruct
    public void init() {
        classPermissionCheckUtil = this;
        classPermissionCheckUtil.studentClassMapper = this.studentClassMapper;
        classPermissionCheckUtil.teacherClassMapper = this.teacherClassMapper;
        classPermissionCheckUtil.signListMapper = this.signListMapper;
        classPermissionCheckUtil.workMapper = this.workMapper;
        classPermissionCheckUtil.studentWorkMapper = this.studentWorkMapper;
        classPermissionCheckUtil.announcementMapper = this.announcementMapper;
    }
}
