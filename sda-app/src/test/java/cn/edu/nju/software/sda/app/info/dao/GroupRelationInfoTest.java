package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.Application;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
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
public class GroupRelationInfoTest {
    @Autowired
    private GroupRelationInfoDao dao;

    @Test
    public void testsaveProfile(){
//        GroupRelationInfo info = new GroupRelationInfo("testname");
//        info.setParentId("1");
//        info.setDesc("desc");
//        info.setStatus(Info.InfoStatus.SAVING);
//        GroupRelation relation = new GroupRelation(2.0);
//        Node node1 = new ClassNode();
//        node1.setId("1");
//        node1.setName("test/node/zzz");
//        node1.setAttrStr("NORMAL");
//        Node node2 = new ClassNode();
//        node1.setId("2");
//        node1.setName("test/node/aa");
//        node1.setAttrStr("NORMAL");
//        relation.addNode(node1);
//        relation.addNode(node2);
//        info.addRelation(relation);
//        GroupRelationInfo info1 = dao.saveProfile(info);
//        System.out.println(info1);
    }


    @Test
    public void testsaveDetail(){
        GroupRelationInfo info = new GroupRelationInfo("testname");
        info.setId("190529DC22XY3TR4");
        info.setParentId("1");
        info.setDesc("desc");
        info.setStatus(Info.InfoStatus.SAVING);
        GroupRelation relation = new GroupRelation(2.0);
        Node node1 = new ClassNode();
//        node1.setId("1");
        node1.setName("se/citerus/dddsample/domain/model/handling/HandlingEvent");
        node1.setAttrStr("NORMAL");
        Node node2 = new ClassNode();
//        node1.setId("2");
        node2.setName("se/citerus/dddsample/application/BookingService");
        node2.setAttrStr("INTERFACE");
        relation.addNode(node1);
        relation.addNode(node2);
        info.addRelation(relation);
        GroupRelationInfo info1 = dao.saveDetail(info);
        System.out.println(info1);
    }


    @Test
    public void testUpdateProfileInfoById(){
        GroupRelationInfo info = new GroupRelationInfo("hah");
        info.setId("1");
        info.setName("haha");
        GroupRelationInfo infos = dao.updateProfileInfoById(info);
        System.out.println(infos);
    }

    @Test
    public void testqueryProfileInfoById(){
        GroupRelationInfo info = dao.queryProfileInfoById("1");
        System.out.println(info);
    }

    @Test
    public void testqueryProfileInfoByAppId(){
        List<GroupRelationInfo> infos = dao.queryProfileInfoByAppId("1");
        System.out.println(infos);
    }

    @Test
    public void testQueryProfileInfoByAppIdAndInfoName(){
        List<GroupRelationInfo> infos = dao.queryProfileInfoByAppIdAndInfoName("1","haha");
        System.out.println(infos);
    }

    @Test
    public void testqueryDetailInfoById(){
        GroupRelationInfo infos = dao.queryDetailInfoById("1");
        System.out.println(infos);
    }
}
