package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;


/**
 * @author pedro
 * @since 2019-12-02
 */
@Repository
public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 查询用户名为$username的人数
     *
     * @param username 用户名
     * @return 人数
     */
    int selectCountByUsername(String username);

    /**
     * 查询用户id为$id的人数
     *
     * @param id 用户id
     * @return 人数
     */
    int selectCountById(Long id);

    /**
     * 通过分组id分页获取用户数据
     *
     * @param pager   分页
     * @param groupId 分组id
     * @param rootGroupId 超级用户组id(不返回超级用户组的用户)
     * @return 分页数据
     */
    IPage<UserDO> selectPageByGroupId(Page pager, Long groupId, Long rootGroupId);

    /**
     * 通过班级id分页获取用户数据
     *
     * @param pager   分页
     * @param classId 班级id
     * @return 分页数据
     */
    IPage<UserDO> selectPageByClassId(Page pager, Long classId);

    /**
     * 通过班级id分页获取非此班级学生数据
     *
     * @param pager   分页
     * @param classId 班级id
     * @return 分页数据
     */
    IPage<UserDO> selectFreshUserPageByClassId(Page pager, Long classId);

    /**
     * 通过班级id分页和姓名获取非此班级学生数据
     *
     * @param pager   分页
     * @param classId 班级id
     * @param name    名字
     * @return 分页数据
     */
    IPage<UserDO> selectFreshUserPageByClassIdAndName(Page pager, @Param("classId") Long classId, @Param("name") String name);
}