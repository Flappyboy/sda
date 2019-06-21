package cn.edu.nju.software.sda.app.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mvc;

    private String appId = null;

    @Before
    public void setUp() throws Exception {
        Long time = System.currentTimeMillis();
        AppControllerTest.addApp(mvc, "testTask", "");
        JSONObject app = AppControllerTest.queryApp(mvc, "testTask", time);
        appId = app.getString("id");

    }

    @After
    public void tearDown() throws Exception {
        AppControllerTest.deleteApp(mvc, appId);
    }

    @Test
    public void doTask() throws Exception {
        InputStream inputStream = TaskControllerTest.class.getClassLoader().getResourceAsStream("dddsample-2.0-SNAPSHOT.jar");
        MockMultipartFile firstFile = new MockMultipartFile("file",
                "dddsample-2.0-SNAPSHOT.jar", "text/plain", inputStream);

        String result = mvc.perform(MockMvcRequestBuilders.multipart("/api/upload")
                .file(firstFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JSONObject file = JSONObject.parseObject(result);
        String filePath = file.getString("path");

        result = mvc.perform(MockMvcRequestBuilders.post("/api/task/do")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"appId\":\""+appId+"\",\"type\":\"InfoCollection\",\"functionName\"" +
                        ":\"sys_StaticJavaInfoCollection\",\"inputDataDto\":" +
                        "{\"infoDatas\":{},\"formDatas\":{\"Package\":[\"se.citerus.dddsample\"]," +
                        "\"Jar/War\":[\""+filePath+"\"]}}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JSONObject task = JSONObject.parseObject(result);
        String taskId = task.getString("id");
        assert (StringUtils.isNotBlank(taskId));

        String status = task.getString("status");
        assert (StringUtils.isNotBlank(status));
        // /api/task/1906219345RW2AK4
        Long time = System.currentTimeMillis();
        while("Doing".equals(status)) {
            assert (System.currentTimeMillis() - time < 3000);

            Thread.sleep(200);
            result = mvc.perform(MockMvcRequestBuilders
                    .get("/api/task/" + taskId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            task = JSONObject.parseObject(result);

            status = task.getString("status");
            assert (StringUtils.isNotBlank(status));
            if("Complete".equals(status)){
                break;
            }
            assert (!"Error".equals(status));
        }
    }
}