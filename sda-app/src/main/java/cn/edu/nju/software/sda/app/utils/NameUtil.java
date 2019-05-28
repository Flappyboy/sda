package cn.edu.nju.software.sda.app.utils;

public class NameUtil {
    public static String getPackageNameFromClassName(String className){
        return className.substring(0,className.lastIndexOf("/"));
    }
    public static String getSimpleNameFromClassName(String className){
        return className.substring(className.lastIndexOf("/")+1,className.length());
    }
}
