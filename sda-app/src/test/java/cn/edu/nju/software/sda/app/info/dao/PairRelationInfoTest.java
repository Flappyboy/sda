package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.Application;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})// 指定启动类
//@ActiveProfiles("test")
public class PairRelationInfoTest {
    @Autowired
    private PairRelationInfoDao dao;
    @Test
    public void testupdateProfileInfoById(){
        PairRelationInfo info = new PairRelationInfo("hah");
        info.setId("190522FT9R1K2M3C");
        info.setName("haha");
        PairRelationInfo infos = dao.updateProfileInfoById(info);
        System.out.println(infos);
    }

    @Test
    public void testqueryProfileInfoById(){
        PairRelationInfo info = dao.queryProfileInfoById("190522FT9SW8CHM8");
        System.out.println(info);
    }

    @Test
    public void testqueryProfileInfoByAppId(){
        List<PairRelationInfo> infos = dao.queryProfileInfoByAppId("1");
        System.out.println(infos);
    }

    @Test
    public void testQueryProfileInfoByAppIdAndInfoName(){
        List<PairRelationInfo> infos = dao.queryProfileInfoByAppIdAndInfoName("1","SYS_RELATION_PAIR_STATIC_ClASS_CALL_COUNT");
        System.out.println(infos);
    }

    @Test
    public void testqueryDetailInfoById(){
        PairRelationInfo infos = dao.queryDetailInfoById("190522FT9R1K2M3C");
        System.out.println(infos);
    }
}
