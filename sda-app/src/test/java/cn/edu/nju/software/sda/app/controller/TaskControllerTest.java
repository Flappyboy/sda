package cn.edu.nju.software.sda.app.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
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

    //完整的测试了从静态数据收集到插件生成gitcommit到最后的Louvain划分
    @Test
    public void processForStaticPluginGitLouvain() throws Exception {
        javaStatic(mvc, appId);
        pinpointPlugin(mvc, appId);
    }

    public static void waitTask(MockMvc mvc, JSONObject task, Long waitTime) throws Exception {
        String taskId = task.getString("id");
        assert (StringUtils.isNotBlank(taskId));

        String status = task.getString("status");
        assert (StringUtils.isNotBlank(status));

        Long time = System.currentTimeMillis();
        while("Doing".equals(status)) {
            assert (System.currentTimeMillis() - time < waitTime);

            Thread.sleep(200);
            String result = mvc.perform(MockMvcRequestBuilders
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

    public static void javaStatic(MockMvc mvc, String appId) throws Exception {
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

        waitTask(mvc, task, 3000l);
    }

    public static void pinpointPlugin(MockMvc mvc, String appId) throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.post("/api/task/do")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"appId\":\""+appId+"\",\"type\":\"InfoCollection\"," +
                        "\"functionName\":\"sys_PinpointPluginInfoCollection\"," +
                        "\"inputDataDto\":{\"infoDatas\":{\"SYS_NODE\":" +
                        "[{\"id\":\""+appId+"\",\"name\":\"SYS_NODE\"}]}," +
                        "\"formDatas\":{\"Plugin Name\":[\"Cargo\"]," +
                        "\"Service Type\":[\"1900\"]," +
                        "\"Key Code\":[\"900\"]}}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JSONObject task = JSONObject.parseObject(result);
        waitTask(mvc, task, 120000l);
    }

    public void downloadPlugin(){
        // /api/info/download?id=190621BZXT269XGC&name=SYS_PINPOINT_PLUGIN_INFO

    }

    public void gitCommit(){

    }

    public void queryInfo(){

    }
}