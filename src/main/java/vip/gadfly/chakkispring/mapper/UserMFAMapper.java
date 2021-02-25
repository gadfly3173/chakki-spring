package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.gadfly.chakkispring.model.UserMFADO;

@Repository
public interface UserMFAMapper extends BaseMapper<UserMFADO> {

    String getSecretKeyByUserId(@Param("userId") Integer userId);

}
