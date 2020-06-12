package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.gadfly.chakkispring.bo.ClassPermissionBO;
import vip.gadfly.chakkispring.dto.admin.*;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.PermissionDO;
import vip.gadfly.chakkispring.model.UserDO;

import java.util.List;
import java.util.Map;

/**
 * @author pedro
 * @since 2019-11-30
 */
public interface ClassService {

    /**
     * 通过班级id分页获取用户数据
     *
     * @param classId 班级id
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<UserDO> getUserPageByClassId(Long classId, Long count, Long page);

    /**
     * 分页获取班级数据
     *
     * @param page  当前页
     * @param count 当前页数量
     * @return 班级数据
     */
    IPage<ClassDO> getClassPage(Long page, Long count);

    /**
     * 获得班级数据
     *
     * @param id 班级id
     * @return 班级数据
     */
    ClassDO getClass(Long id);

    /**
     * 新建班级
     *
     * @param dto 班级信息
     * @return 是否成功
     */
    boolean createClass(NewClassDTO dto);

    /**
     * 更新班级
     *
     * @param id  班级id
     * @param dto 班级信息
     * @return 是否成功
     */
    boolean updateClass(Long id, UpdateClassDTO dto);

    /**
     * 删除班级
     *
     * @param id 班级id
     * @return 是否成功
     */
    boolean deleteClass(Long id);

    /**
     * 获得所有班级信息
     */
    List<ClassDO> getAllClasses();
}
