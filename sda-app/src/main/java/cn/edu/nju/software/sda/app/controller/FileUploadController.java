package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
//@RestController
@Api(value = "文件上传接口")
@RequestMapping(value = "/api")
@Controller
public class FileUploadController{

    /*
     * 获取file.html页面
     */
    @RequestMapping(value ="file",method = RequestMethod.GET)
    public String file(){
        return "/file";
    }

    //处理文件上传
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传", notes = "返回状态200成功")
    @ResponseBody
    public ResponseEntity uploadImg(@RequestParam("file") MultipartFile file,
                                    HttpServletRequest request) throws UnsupportedEncodingException {
        String fileName = file.getOriginalFilename();  //图片名字

        String path = WorkspaceUtil.path();

//        String filePath = "/Users/yaya/Desktop/upload/";
        String filePath = path+"/upload/";
        System.out.println("======:   "+path);

        //调用文件处理类FileUtil，处理文件，将文件写入指定位置
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Map<String, String> result = new HashMap<String, String>();
        result.put("path", URLEncoder.encode(WorkspaceUtil.relativePath(filePath + fileName), "utf-8"));
        // 返回图片的存放路径
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/download")
    public String downloadFile(String path, HttpServletResponse response) throws UnsupportedEncodingException {
        File file = new File(URLDecoder.decode(WorkspaceUtil.absolutePath(path), "utf-8"));

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
