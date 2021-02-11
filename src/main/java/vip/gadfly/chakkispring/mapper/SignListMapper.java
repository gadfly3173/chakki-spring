package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.SignListDO;
import vip.gadfly.chakkispring.vo.SignCountVO;
import vip.gadfly.chakkispring.vo.SignListVO;

/**
 * @author Gadfly
 */
@Repository
public interface SignListMapper extends BaseMapper<SignListDO> {
    /**
     * 通过班级id分页获取签到项目
     *
     * @param pager   分页
     * @param classId 班级id
     * @return 分页数据
     */
    IPage<SignListVO> selectSignPageByClassId(Page pager, Integer classId);

    /**
     * 通过班级id获取最新签到项目
     *
     * @param classId 班级id
     * @return 数据
     */
    SignListVO getLatestSignByClassId(Integer classId);

    /**
     * 学生通过班级id获取最新签到项目
     *
     * @param classId 班级id
     * @return 数据
     */
    SignListVO getStudentLatestSignByClassId(Integer classId, Integer userId);

    SignCountVO selectSignCountInfoById(@Param("signId") Integer signId);
}
