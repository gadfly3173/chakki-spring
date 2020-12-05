package vip.gadfly.chakkispring.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import vip.gadfly.chakkispring.common.mybatis.Page;
import vip.gadfly.chakkispring.model.LogDO;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class LogMapperTest {


    private final String permission = "查看lin的信息";
    private final String message = "就是个瓜皮";
    private final String method = "GET";
    private final String path = "/";
    private final Integer statusCode = 200;
    private final Long userId = 1L;
    private final String username = "super";
    @Autowired
    private LogMapper logMapper;
    private Date start = new Date();

    @Before
    public void setUp() throws Exception {
        LogDO logDO = new LogDO();
        logDO.setPermission(permission);
        logDO.setMessage(message);
        logDO.setMethod(method);
        logDO.setPath(path);
        logDO.setStatusCode(statusCode);
        logDO.setUserId(userId);
        logDO.setUsername(username);
        logDO.setCreateTime(start);
        long ll = start.getTime() - 500000;
        // start.setTime(ll);
        start = new Date(ll);
        logMapper.insert(logDO);
    }

    @Test
    public void testFindLogsByUsernameAndRange() {
        Date now = new Date();
        Page page = new Page(0, 10);
        IPage<LogDO> iPage = logMapper.findLogsByUsernameAndRange(page, username, start, now);
        List<LogDO> logs = iPage.getRecords();
        assertTrue(logs.size() > 0);
    }

    @Test
    public void testFindLogsByUsernameAndRange1() {
        long changed = start.getTime();
        Date ch = new Date(changed - 1000);
        Date ch1 = new Date(changed - 2000);
        Page page = new Page(1, 10);
        IPage<LogDO> iPage = logMapper.findLogsByUsernameAndRange(page, username, ch1, ch);
        List<LogDO> logs = iPage.getRecords();
        assertTrue(logs.size() == 0);
    }

    @After
    public void tearDown() throws Exception {
    }
}