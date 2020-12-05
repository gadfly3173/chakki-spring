package vip.gadfly.chakkispring.mapper;

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
import vip.gadfly.chakkispring.model.FileDO;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class FileMapperTest {

    private final String md5 = "iiiiilllllll";
    private final String name = "千里之外";
    @Autowired
    private FileMapper fileMapper;

    @Before
    public void setUp() throws Exception {
        FileDO fileDO = new FileDO();
        fileDO.setName(name);
        fileDO.setPath("千里之外...");
        fileDO.setSize(1111);
        fileDO.setExtension(".png");
        fileDO.setMd5(md5);
        fileMapper.insert(fileDO);
    }

    @Test
    public void testFindOneByMd5() {
        FileDO one = fileMapper.selectByMd5(md5);
        assertEquals(one.getName(), name);
    }

    @After
    public void tearDown() throws Exception {
    }
}