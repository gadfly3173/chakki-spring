package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.gadfly.chakkispring.dto.admin.*;
import vip.gadfly.chakkispring.dto.lesson.NewSignDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SignListDO;
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
     * 通过班级id分页获取非此班级学生数据
     *
     * @param classId 班级id
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<UserDO> getFreshUserPageByClassId(Long classId, Long count, Long page);

    /**
     * 通过班级id分页和名字获取非此班级学生数据
     *
     * @param classId 班级id
     * @param name    名字
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<UserDO> getFreshUserPageByClassIdAndName(Long classId, String name, Long count, Long page);

    /**
     * 获得用户的所有班级
     *
     * @param userId 用户id
     * @return 所有班级
     */
    List<ClassDO> getUserClassByUserId(Long userId);

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

    /**
     * 删除用户与班级直接的关联
     *
     * @param userId    用户id
     * @param deleteIds 班级id
     */
    boolean deleteStudentClassRelations(Long userId, List<Long> deleteIds);

    /**
     * 添加用户与班级直接的关联
     *
     * @param classId 班级id
     * @param addIds 用户id
     */
    boolean addStudentClassRelations(Long classId, List<Long> addIds);

    /**
     * 添加用户与班级直接的关联
     *
     * @param validator 新签到内容
     */
    boolean createSign(NewSignDTO validator);

    /**
     * 通过班级id分页获取签到项目
     *
     * @param classId 班级id
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<SignListDO> getSignPageByClassId(Long classId, Long count, Long page);

    /**
     * 通过id分页获取签到项目详情
     *
     * @param signId  签到项目id
     * @param signedStatus  签到状态：0-全部，1-已签到，2-未签到
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    List<SignListDO> getSignDetailPageById(Long signId, int signedStatus, Long count, Long page);
}
