package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.Application;
import cn.edu.nju.software.sda.app.entity.Git;
import cn.edu.nju.software.sda.app.service.GitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
public class GitServiceImplTest {

    @Autowired
    GitService gitService;

    @Test
    public void statisticsPartitionResultEdge() {
        List<Git> gitList = gitService.queryGitByAppId("190216G9CMGFD680");
        System.out.println(gitList.get(0));
    }

}