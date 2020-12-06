package vip.gadfly.chakkispring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.gadfly.chakkispring.dto.admin.NewClassDTO;
import vip.gadfly.chakkispring.dto.admin.NewSemesterDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateClassDTO;
import vip.gadfly.chakkispring.dto.admin.UpdateSemesterDTO;
import vip.gadfly.chakkispring.dto.lesson.NewSignDTO;
import vip.gadfly.chakkispring.dto.lesson.UpdateSignRecordDTO;
import vip.gadfly.chakkispring.model.ClassDO;
import vip.gadfly.chakkispring.model.SemesterDO;
import vip.gadfly.chakkispring.model.SignListDO;
import vip.gadfly.chakkispring.model.UserDO;
import vip.gadfly.chakkispring.vo.SignCountVO;
import vip.gadfly.chakkispring.vo.StudentSignVO;
import vip.gadfly.chakkispring.vo.TeacherClassVO;

import java.util.List;

/**
 * @author Gadfly
 */
public interface ClassService {

    /**
     * 通过班级id分页获取用户数据
     *
     * @param classId 班级id
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<UserDO> getUserPageByClassId(Long classId, Long count, Long page);

    /**
     * 通过班级id分页获取非此班级学生数据
     *
     * @param classId 班级id
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<UserDO> getFreshUserPageByClassId(Long classId, Long count, Long page);

    /**
     * 通过班级id分页和名字获取非此班级学生数据
     *
     * @param classId 班级id
     * @param name    名字
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<UserDO> getFreshUserPageByClassIdAndName(Long classId, String name, Long count, Long page);

    /**
     * 获得用户的所有班级
     *
     * @param userId 用户id
     * @return 所有班级
     */
    List<ClassDO> getUserClassByUserId(Long userId);

    /**
     * 分页获取班级数据
     *
     * @param page  当前页
     * @param count 当前页数量
     * @return 班级数据
     */
    IPage<ClassDO> getClassPage(Long page, Long count);

    /**
     * 获得班级数据
     *
     * @param id 班级id
     * @return 班级数据
     */
    ClassDO getClass(Long id);

    /**
     * 新建班级
     *
     * @param dto 班级信息
     * @return 是否成功
     */
    boolean createClass(NewClassDTO dto);

    /**
     * 更新班级
     *
     * @param id  班级id
     * @param dto 班级信息
     * @return 是否成功
     */
    boolean updateClass(Long id, UpdateClassDTO dto);

    /**
     * 删除班级
     *
     * @param id 班级id
     * @return 是否成功
     */
    boolean deleteClass(Long id);

    /**
     * 获得所有班级信息
     */
    List<ClassDO> getAllClasses();

    /**
     * 删除用户与班级之间的关联
     *
     * @param userId    用户id
     * @param deleteIds 班级id
     */
    boolean deleteStudentClassRelations(Long userId, List<Long> deleteIds);

    /**
     * 添加用户与班级之间的关联
     *
     * @param classId 班级id
     * @param addIds  用户id
     */
    boolean addStudentClassRelations(Long classId, List<Long> addIds);

    /**
     * 添加用户与班级直接的关联
     *
     * @param validator 新签到内容
     */
    boolean createSign(NewSignDTO validator);

    /**
     * 通过班级id分页获取签到项目
     *
     * @param classId 班级id
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<SignListDO> getSignPageByClassId(Long classId, Long count, Long page);

    /**
     * 通过id分页获取签到项目详情
     *
     * @param signId     签到项目id
     * @param signStatus 筛选签到状态：0-全部，false-未签到
     * @param count      当前页数目
     * @param page       当前分页
     * @return 用户数据
     */
    IPage<StudentSignVO> getUserPageBySignId(Long signId, Integer signStatus, String username, Long count, Long page,
                                             boolean orderByIP);

    /**
     * 获得签到数据
     *
     * @param id 签到id
     * @return 签到数据
     */
    SignCountVO getSign(Long id);

    /**
     * 修改签到记录信息
     *
     * @param validator 签到内容
     */
    boolean updateSignRecord(UpdateSignRecordDTO validator, Long signId);

    /**
     * 通过班级id分页获取班级内的教师
     *
     * @param classId 班级id
     * @return 用户数据
     */
    IPage<TeacherClassVO> getTeacherPageByClassId(Long classId);

    /**
     * 通过姓名、班级id分页获取不在班级的教师
     *
     * @param classId 班级id
     * @param name    名字
     * @param count   当前页数目
     * @param page    当前分页
     * @return 用户数据
     */
    IPage<UserDO> getFreshTeacherPageByClassIdAndName(Long classId, String name, Long count, Long page);

    /**
     * 添加教师与班级之间的关联
     *
     * @param classIds 班级id
     * @param userId  用户id
     */
    boolean deleteTeacherClassRelations(Long userId, List<Long> classIds);

    /**
     * 添加教师与班级之间的关联
     *
     * @param classId 班级id
     * @param userIds  用户id
     */
    boolean addTeacherClassRelations(Long classId, List<Long> userIds, Integer level);

    /**
     * 新建学期
     *
     * @param validator 学期信息
     * @return 是否成功
     */
    boolean createSemester(NewSemesterDTO validator);

    /**
     * 全部学期
     *
     * @return 列表
     */
    List<SemesterDO> getAllSemesters();

    /**
     * 更新学期
     *
     * @param validator 学期信息
     * @return 是否成功
     */
    boolean updateSemester(Long id, UpdateSemesterDTO validator);

    /**
     * 删除学期
     *
     * @param id 学期id
     * @return 是否成功
     */
    boolean deleteSemester(Long id);

    /**
     * 根据学期id和教师id查询班级列表
     *
     * @param semesterId 学期id
     * @param teacherId 教师id
     * @return 班级列表
     */
    List<ClassDO> getClassesBySemesterAndTeacher(Long semesterId, Long teacherId);

    /**
     * 根据学期id和学生id查询班级列表
     *
     * @param semesterId 学期id
     * @param userId 学生id
     * @return 班级列表
     */
    List<ClassDO> getClassesBySemesterAndStudent(Long semesterId, Long userId);
}
