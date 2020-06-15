package vip.gadfly.chakkispring.service;

import vip.gadfly.chakkispring.model.ClassDO;

import java.util.List;

/**
 * @author Gadfly
 */

public interface StudentService {

    /**
     * 获得学生班级列表数据
     *
     * @return 分组数据
     */
    List<ClassDO> getStudentClassList();
}
