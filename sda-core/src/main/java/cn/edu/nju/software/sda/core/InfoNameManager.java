package cn.edu.nju.software.sda.core;

import cn.edu.nju.software.sda.core.domain.info.Info;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoNameManager {

    private static Map<String, Class> infoClassNameMap = new HashMap<>();

    private static Map<String, String> infoDescMap = new HashMap<>();

    public static void register(String name, Class clazz){
        if(clazz == null){
            throw new RuntimeException("clazz is null");
        }
        if(!Info.class.isAssignableFrom(clazz)){
            throw new RuntimeException("clazz "+clazz.getName()+" is not subclass of Info");
        }
        infoClassNameMap.put(name, clazz);
    }
    public static void register(String name, Class clazz, String desc){
        register(name, clazz);
        infoDescMap.put(name, desc);
    }

    public static String getDescForInfoName(String name){
        return infoDescMap.get(name);
    }

    public static Map<String, String> getInfoDescMap() {
        return infoDescMap;
    }

    public static Class getClassByName(String name){
        return infoClassNameMap.get(name);
    }

    public static List<String> getNamesByClass(Class clazz){
        List<String> names = new ArrayList<>();
        for (Map.Entry<String, Class> entry:
            infoClassNameMap.entrySet()){
            if(clazz.equals(entry.getValue())) {
                names.add(entry.getKey());
            }
        }
        return names;
    }

    public static Map<String, Class> getInfoClassNameMap() {
        return infoClassNameMap;
    }
}
