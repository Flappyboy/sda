package cn.edu.nju.software.sda.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AppControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void app() throws Exception{
        Long time = System.currentTimeMillis();
        addApp(mvc, "test", "desc");
        JSONObject app = queryApp(mvc, "test", time);
        assert (app != null);
        String appId = app.getString("id");
        String desc = app.getString("desc");
        assert ("desc".equals(desc));

        JSONObject putedApp = putApp(mvc, appId, "testNew", "update desc");
        assert (putedApp != null);
        String name = putedApp.getString("name");
        assert ("testNew".equals(name));
        desc = putedApp.getString("desc");
        assert ("update desc".equals(desc));

        deleteApp(mvc, appId);
    }

    public static void addApp(MockMvc mvc, String name, String desc) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/app")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"name\":\""+name+"\",\"desc\":\""+desc+"\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public static JSONObject queryApp(MockMvc mvc, String name, Long time) throws Exception{
        String result = mvc.perform(MockMvcRequestBuilders
                .get("/api/app?name="+name+"&desc=&pageSize=10&pageNum=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray jsonArray = (JSONArray) jsonObject.get("result");
        for (Object object :
                jsonArray) {
            JSONObject app = (JSONObject) object;
            if(name.equals(app.getString("name"))){
                assert (time.compareTo(app.getLong("createdAt")) < 0);
                return app;
            }
        }
        return null;
    }

    public static JSONObject putApp(MockMvc mvc, String id, String name, String desc) throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.put("/api/app")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"name\":\""+name+"\",\"desc\":\""+desc+"\",\"id\":\""+id+"\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return JSONObject.parseObject(result);
    }

    public static void deleteApp(MockMvc mvc, String id) throws Exception {
        JSONObject app = getAppById(mvc, id);
        System.out.println(app);
        mvc.perform(MockMvcRequestBuilders
                .delete("/api/app/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk());
        app = getAppById(mvc, id);
        System.out.println(app);

    }

    public static JSONObject getAppById(MockMvc mvc, String id) throws Exception{
        String result = mvc.perform(MockMvcRequestBuilders
                .get("/api/app/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return JSONObject.parseObject(result);
    }
}