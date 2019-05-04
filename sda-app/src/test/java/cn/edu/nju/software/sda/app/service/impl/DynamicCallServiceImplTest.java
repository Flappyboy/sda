package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.Application;
import cn.edu.nju.software.sda.app.entity.DynamicAnalysisInfo;
import cn.edu.nju.software.sda.app.service.DynamicCallService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
public class DynamicCallServiceImplTest {
    @Autowired
    DynamicCallService dynamicCallService;

    @Test
    public void statisticsPartitionResultEdge() {
        DynamicAnalysisInfo dynamicAnalysisInfo = new DynamicAnalysisInfo();
        dynamicAnalysisInfo.setId("1");
        dynamicAnalysisInfo.setAppid("1902168DCWHRK86W");
        //dynamicCallService.statisticsCallInfo(dynamicAnalysisInfo);
    }

}