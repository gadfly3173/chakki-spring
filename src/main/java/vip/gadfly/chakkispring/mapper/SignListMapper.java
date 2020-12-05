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
    IPage<SignListDO> selectSignPageByClassId(Page pager, Long classId);

    /**
     * 通过班级id获取最新签到项目
     *
     * @param classId 班级id
     * @return 数据
     */
    SignListVO getLatestSignByClassId(Long classId);

    /**
     * 学生通过班级id获取最新签到项目
     *
     * @param classId 班级id
     * @return 数据
     */
    SignListVO getStudentLatestSignByClassId(Long classId, Long userId);

    SignCountVO selectSignCountInfoById(@Param("signId") Long signId);
}
