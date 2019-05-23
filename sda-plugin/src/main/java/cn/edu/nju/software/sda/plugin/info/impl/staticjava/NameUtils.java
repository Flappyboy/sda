package cn.edu.nju.software.sda.plugin.info.impl.staticjava;

public class NameUtils {

    public static String methodFullName(String owner, String name, String desc){
        return owner + "." + name + desc;
    }
}
