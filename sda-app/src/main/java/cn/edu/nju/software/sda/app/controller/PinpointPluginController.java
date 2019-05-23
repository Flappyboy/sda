package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.plugin.Generate;
import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.service.AppService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@CrossOrigin
@RestController
@Api(value = "项目App相关接口")
@RequestMapping(value = "/api")
public class PinpointPluginController {

    @Autowired
    AppService appService;

    //实现Spring Boot 的文件下载功能，映射网址为/download
    @RequestMapping("/plugin/download/{id}")
    public String downloadFile(@PathVariable String id,
                               HttpServletResponse response) throws UnsupportedEncodingException {
        AppEntity app = appService.queryAppById(id);
        Generate generate = new Generate(app.getName(), app.getId());
        // 获取指定目录下的第一个文件
        File file = generate.getJar();


            // 如果文件名存在，则进行下载
            if (file.exists()) {

                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));

                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download the song successfully!");
                }
                catch (Exception e) {
                    System.out.println("Download the song failed!");
                }
                finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        return null;
    }

}
