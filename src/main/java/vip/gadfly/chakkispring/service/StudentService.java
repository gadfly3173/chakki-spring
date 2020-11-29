package vip.gadfly.chakkispring.service;

import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.vo.SignListVO;

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

    /**
     * 学生签到
     *
     * @param signId 签到id
     */
    boolean confirmSign(Long signId, String ip);

    /**
     * 获得最新签到项目
     *
     * @return 签到项目数据
     */
    SignListVO getLatestSignByClassId(Long classId);

    boolean signAvailable(Long signId);
}
