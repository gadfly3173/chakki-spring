package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.ClassDO;

import java.util.List;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Repository
public interface ClassMapper extends BaseMapper<ClassDO> {

    /**
     * 获得用户的所有班级
     *
     * @param userId 用户id
     * @return 所有班级
     */
    List<ClassDO> selectUserClasses(@Param("userId") Long userId);

    /**
     * 获得用户的所有班级id
     *
     * @param userId 用户id
     * @return 所有班级id
     */
    List<Long> selectUserClassIds(@Param("userId") Long userId);

    /**
     * 通过id获得班级个数
     *
     * @param id id
     * @return 个数
     */
    int selectCountById(@Param("id") Long id);

    /**
     * 检查用户是否在该名称的班级里
     *
     * @param userId    用户id
     * @param className 班级名
     */
    int selectCountUserByUserIdAndClassName(@Param("userId") Long userId, @Param("className") String className);
}
