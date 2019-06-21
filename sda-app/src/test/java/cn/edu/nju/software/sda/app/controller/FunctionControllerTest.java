package cn.edu.nju.software.sda.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class FunctionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void findFunctions() throws Exception {
        // /api/function/Partition
        JSONArray jsonArray = findFunctionsByType("Partition");
        assert (!jsonArray.isEmpty());
        jsonArray = findFunctionsByType("InfoCollection");
        assert (!jsonArray.isEmpty());
        jsonArray = findFunctionsByType("Evaluation");
        assert (!jsonArray.isEmpty());
    }

    private JSONArray findFunctionsByType(String type) throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders
                .get("/api/function/"+type))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return JSONObject.parseArray(result);
    }
}