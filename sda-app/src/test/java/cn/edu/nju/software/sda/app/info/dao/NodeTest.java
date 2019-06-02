package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.Application;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})// 指定启动类
@ActiveProfiles("test")
public class NodeTest {
    @Autowired
    private NodeInfoDao dao;
    @Test
    public void testDelete(){
        NodeInfo info = dao.queryDetailInfoById("190602BGHTW3K8X4");
        System.out.println(info);
    }
}
