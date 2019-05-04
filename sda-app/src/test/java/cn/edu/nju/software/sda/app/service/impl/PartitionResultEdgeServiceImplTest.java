package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.Application;
import cn.edu.nju.software.sda.app.service.PartitionResultEdgeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
public class PartitionResultEdgeServiceImplTest {

    @Autowired
    PartitionResultEdgeService partitionResultEdgeService;

    @Test
    public void statisticsPartitionResultEdge() {
        //partitionResultEdgeService.statisticsPartitionResultEdge("190214953TWR54M8");
    }
}