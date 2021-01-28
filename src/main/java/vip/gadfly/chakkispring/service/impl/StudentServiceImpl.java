package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.common.constant.SignStatusConstant;
import vip.gadfly.chakkispring.common.util.IPUtil;
import vip.gadfly.chakkispring.mapper.ClassMapper;
import vip.gadfly.chakkispring.mapper.SignListMapper;
import vip.gadfly.chakkispring.mapper.StudentSignMapper;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.StudentSignDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.StudentService;
import vip.gadfly.chakkispring.vo.SignListVO;

import java.util.List;

/**
 * @author Gadfly
 */

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private SignListMapper signListMapper;

    @Autowired
    private StudentSignMapper studentSignMapper;

    @Override
    public List<ClassDO> getStudentClassList() {
        UserDO user = LocalUser.getLocalUser();
        return classMapper.selectUserClasses(user.getId());
    }

    @Override
    public boolean confirmSign(Integer signId, String ip) {
        UserDO user = LocalUser.getLocalUser();
        QueryWrapper<StudentSignDO> wrapper = new QueryWrapper<>();
        if (IPUtil.isInternalIp(ip)) {
            wrapper.lambda()
                    .eq(StudentSignDO::getSignId, signId)
                    .and(i -> i.eq(StudentSignDO::getUserId, user.getId())
                            .or()
                            .eq(StudentSignDO::getIp, ip));
        } else {
            wrapper.lambda().eq(StudentSignDO::getSignId, signId).eq(StudentSignDO::getUserId, user.getId());
        }
        if (studentSignMapper.selectCount(wrapper) == 0) {
            return studentSignMapper.insert(new StudentSignDO(signId, user.getId(), ip,
                    SignStatusConstant.STATUS_SIGNED)) > 0;
        }
        return false;
    }

    @Override
    public boolean signAvailable(Integer signId) {
        return signListMapper.selectById(signId).getEndTime().getTime() > System.currentTimeMillis();
    }

    @Override
    public SignListVO getLatestSignByClassId(Integer classId) {
        Integer userId = LocalUser.getLocalUser().getId();
        return signListMapper.getStudentLatestSignByClassId(classId, userId);
    }
}
