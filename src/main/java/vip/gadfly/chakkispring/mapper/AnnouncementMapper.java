package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.AnnouncementDO;
import vip.gadfly.chakkispring.vo.AnnouncementVO;

/**
 * <p>
 * 公告项目表 Mapper 接口
 * </p>
 *
 * @author Gadfly
 * @since 2021-02-08
 */
@Repository
public interface AnnouncementMapper extends BaseMapper<AnnouncementDO> {

    IPage<AnnouncementVO> selectAnnouncementPageByClassId(Page pager, @Param("classId") Integer classId);

    AnnouncementVO selectAnnouncementVOById(@Param("id") Integer id);
}
