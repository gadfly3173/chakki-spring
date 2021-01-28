package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.ClassDO;

import java.util.List;

/**
 * @author Gadfly
 */
@Repository
public interface ClassMapper extends BaseMapper<ClassDO> {

    /**
     * 获得用户的所有班级
     *
     * @param userId 用户id
     * @return 所有班级
     */
    List<ClassDO> selectUserClasses(@Param("userId") Integer userId);

    /**
     * 获得用户的所有班级id
     *
     * @param userId 用户id
     * @return 所有班级id
     */
    List<Integer> selectUserClassIds(@Param("userId") Integer userId);

    /**
     * 通过id获得班级个数
     *
     * @param id id
     * @return 个数
     */
    int selectCountById(@Param("id") Integer id);

    // /**
    //  * 检查用户是否在该名称的班级里
    //  *
    //  * @param userId    用户id
    //  * @param className 班级名
    //  */
    // int selectCountUserByUserIdAndClassName(@Param("userId") Integer userId, @Param("className") String className);

    /**
     * 根据学期id和教师id查询班级列表
     *
     * @param semesterId 学期id
     * @param teacherId  教师id
     * @return 班级列表
     */
    List<ClassDO> selectClassesBySemesterAndTeacher(Integer semesterId, Integer teacherId);

    /**
     * 根据学期id和学生id查询班级列表
     *
     * @param semesterId 学期id
     * @param userId     学生id
     * @return 班级列表
     */
    List<ClassDO> selectClassesBySemesterAndStudent(Integer semesterId, Integer userId);
}
