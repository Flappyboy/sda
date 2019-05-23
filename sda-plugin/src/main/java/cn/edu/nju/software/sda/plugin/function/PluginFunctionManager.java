package cn.edu.nju.software.sda.plugin.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginFunctionManager {

    private static Map<String, PluginFunction> pluginFunctionMap = new HashMap<>();

    public static void register(PluginFunction function){
        pluginFunctionMap.put(function.getName(), function);
    }

    public static PluginFunction get(String name){
        return pluginFunctionMap.get(name);
    }

    public static List<PluginFunction> get(){
        return new ArrayList<>(pluginFunctionMap.values());
    }

    public static List<PluginFunction> get(FunctionType functionType){
        List<PluginFunction> pluginFunctions = new ArrayList<>();
        for (PluginFunction pluginFunction :
                pluginFunctionMap.values()){
            if(pluginFunction.getType().equals(functionType)){
                pluginFunctions.add(pluginFunction);
            }
        }
        return pluginFunctions;
    }

    public static void clear(){
        pluginFunctionMap.clear();
    }
}
