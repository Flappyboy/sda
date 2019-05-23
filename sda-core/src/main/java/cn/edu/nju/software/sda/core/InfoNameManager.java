package cn.edu.nju.software.sda.core;

import cn.edu.nju.software.sda.core.domain.info.Info;

import java.util.HashMap;
import java.util.Map;

public class InfoNameManager {

    public static Map<String, Class> infoClassNameMap = new HashMap<>();

    public static void register(String name, Class clazz){
        if(clazz == null){
            throw new RuntimeException("clazz is null");
        }
        if(!Info.class.isAssignableFrom(clazz)){
            throw new RuntimeException("clazz "+clazz.getName()+" is not subclass of Info");
        }
        infoClassNameMap.put(name, clazz);
    }

    public static Class getClassByName(String name){
        return infoClassNameMap.get(name);
    }
}
