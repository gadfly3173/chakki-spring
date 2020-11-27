package vip.gadfly.chakkispring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.mapper.ClassMapper;
import vip.gadfly.chakkispring.mapper.StudentSignMapper;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.StudentSignDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.service.StudentService;

import java.util.List;

/**
 * @author Gadfly
 */

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private StudentSignMapper studentSignMapper;

    private final int STATUS_SIGNED = 1;
    private final int STATUS_LATE = 2;
    private final int STATUS_CANCEL = 3;

    @Override
    public List<ClassDO> getStudentClassList() {
        UserDO user = LocalUser.getLocalUser();
        return classMapper.selectUserClasses(user.getId());
    }

    @Override
    public boolean confirmSign(Long signId) {
        UserDO user = LocalUser.getLocalUser();
        QueryWrapper<StudentSignDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StudentSignDO::getUserId, user.getId());
        if (studentSignMapper.selectCount(wrapper) == 0) {
            return studentSignMapper.insert(new StudentSignDO(signId, user.getId(), STATUS_SIGNED)) > 0;
        }
        return false;
    }
}
