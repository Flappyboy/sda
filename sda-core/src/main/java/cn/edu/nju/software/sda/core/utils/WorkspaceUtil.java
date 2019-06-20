package cn.edu.nju.software.sda.core.utils;

import cn.edu.nju.software.sda.core.config.SdaConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;

import java.io.File;
import java.util.UUID;

public class WorkspaceUtil {

    public static void init(){
        String path = path();
        File dir = new File(path);
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdirs();
        }
    }

    public static File tmp_workspace(String name){
        String dirName = path()+"/tmp/"+ name +"/"+ Sid.nextShort();
        File file = new File(dirName);
        file.mkdirs();
        return file;
    }

    public static File workspace(String name){
        String dirName = path()+"/work/"+ name;
        File file = new File(dirName);
        file.mkdirs();
        return file;
    }

    public static String path(){
        String path = SdaConfig.getProperty(SdaConfig.PATH);
        if(StringUtils.isBlank(path)){
            throw new RuntimeException("path为空");
        }
        return path;
    }

    public static String relativePath(String path){
        return StringUtils.replaceOnce(path, SdaConfig.getProperty(SdaConfig.PATH), "");
    }

    public static String absolutePath(String path){
        return SdaConfig.getProperty(SdaConfig.PATH)+path;
    }
}
