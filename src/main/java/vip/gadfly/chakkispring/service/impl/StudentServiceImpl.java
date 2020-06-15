package vip.gadfly.chakkispring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gadfly.chakkispring.common.LocalUser;
import vip.gadfly.chakkispring.mapper.ClassMapper;
import vip.gadfly.chakkispring.model.ClassDO;
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

    @Override
    public List<ClassDO> getStudentClassList() {
        UserDO user = LocalUser.getLocalUser();
        return classMapper.selectUserClasses(user.getId());
    }
}
