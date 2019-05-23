package cn.edu.nju.software.sda.plugin.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoCollectionManager {

    private static Map<String, InfoCollection> infoCollectionPluginMap = new HashMap<>();

    public static void register(InfoCollection infoCollection){
        infoCollectionPluginMap.put(infoCollection.getName(), infoCollection);
    }

    public static InfoCollection get(String name){
        return infoCollectionPluginMap.get(name);
    }

    public static List<InfoCollection> get(){
        return new ArrayList<>(infoCollectionPluginMap.values());
    }

    public static void clear(){
        infoCollectionPluginMap.clear();
    }
}
