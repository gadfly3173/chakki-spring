package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.gadfly.chakkispring.model.ClassDO;

import java.util.List;

/**
 * @author Gadfly
 */
public interface ClassManageService extends IService<ClassDO> {

    /**
     * 获得用户的所有班级
     *
     * @param userId 用户id
     * @return 所有班级
     */
    List<ClassDO> getUserClassesByUserId(Long userId);

    /**
     * 获得用户的所有班级id
     *
     * @param userId 用户id
     * @return 所有班级id
     */
    List<Long> getUserClassIdsByUserId(Long userId);

    /**
     * 分页获取班级数据
     *
     * @param count 分页数量
     * @param page  那一页
     * @return 班级页
     */
    IPage<ClassDO> getClassPage(long page, long count);

    /**
     * 通过id查找班级
     *
     * @param id 班级id
     * @return 内容
     */
    ClassDO getClassById(Long id);

    /**
     * 通过id检查班级是否存在
     *
     * @param id 班级id
     * @return 是否存在
     */
    boolean checkClassExistById(Long id);

    /**
     * 通过名称检查班级是否存在
     *
     * @param name 班级名
     * @return 是否存在
     */
    boolean checkClassExistByName(String name);

    /**
     * 删除用户与班级直接的关联
     *
     * @param userId    用户id
     * @param deleteIds 班级id
     */
    boolean deleteUserClassRelations(Long userId, List<Long> deleteIds);

    /**
     * 获得班级下所有用户的id
     *
     * @param id 班级id
     * @return 用户id
     */
    List<Long> getClassUserIds(Long id);

    /**
     * 删除用户与班级直接的关联
     *
     * @param userId    用户id
     * @param classIds 班级id
     */
    boolean deleteTeacherClassRelations(Long userId, List<Long> classIds);

    /**
     * 根据学期id和教师id查询班级列表
     *
     * @param semesterId 学期id
     * @param teacherId 教师id
     * @return 班级列表
     */
    List<ClassDO> getClassesBySemesterAndTeacher(Long semesterId, Long teacherId);

    /**
     * 根据学期id和学生id查询班级列表
     *
     * @param semesterId 学期id
     * @param userId 学生id
     * @return 班级列表
     */
    List<ClassDO> getClassesBySemesterAndStudent(Long semesterId, Long userId);
}
