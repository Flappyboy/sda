package cn.edu.nju.software.sda.app.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class PartitionNodeEntityControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void partitionResultDetail() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.get("/api/partition-detail/190214953TWR54M8"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print()) ;
        //.andExpect(MockMvcResultMatchers.content().string("365"));  //测试接口返回内容
    }
}