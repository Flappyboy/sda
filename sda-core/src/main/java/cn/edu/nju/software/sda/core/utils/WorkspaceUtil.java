package cn.edu.nju.software.sda.core.utils;

import cn.edu.nju.software.sda.core.config.SdaConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.UUID;

public class WorkspaceUtil {

    public static File workspace(String name){
        String dirName = path()+"/"+ name +"/"+ UUID.randomUUID();
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
