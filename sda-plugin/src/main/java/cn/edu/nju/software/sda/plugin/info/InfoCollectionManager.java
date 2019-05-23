package cn.edu.nju.software.sda.plugin.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoCollectionManager {

    private static Map<String, InfoCollectionPlugin> infoCollectionPluginMap = new HashMap<>();

    public static void register(InfoCollectionPlugin infoCollectionPlugin){
        infoCollectionPluginMap.put(infoCollectionPlugin.getName(), infoCollectionPlugin);
    }

    public static InfoCollectionPlugin get(String name){
        return infoCollectionPluginMap.get(name);
    }

    public static List<InfoCollectionPlugin> get(){
        return new ArrayList<>(infoCollectionPluginMap.values());
    }

    public static void clear(){
        infoCollectionPluginMap.clear();
    }
}
