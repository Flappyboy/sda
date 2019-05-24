package cn.edu.nju.software.sda.plugin.function;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.core.service.FunctionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginFunctionManager {

    private static Map<String, FunctionService> pluginFunctionMap = new HashMap<>();

    public static void register(FunctionService function){
        pluginFunctionMap.put(function.getName(), function);
    }

    public static FunctionService get(String name){
        return pluginFunctionMap.get(name);
    }

    public static ResultDto check(String name, InputData inputData){
        return pluginFunctionMap.get(name).check(inputData);
    }

    public static List<FunctionService> get(){
        return new ArrayList<>(pluginFunctionMap.values());
    }

    public static List<FunctionService> get(FunctionType functionType){
        List<FunctionService> functionServices = new ArrayList<>();
        for (FunctionService functionService :
                pluginFunctionMap.values()){
            if(functionService.getType().equals(functionType)){
                functionServices.add(functionService);
            }
        }
        return functionServices;
    }

    public static void clear(){
        pluginFunctionMap.clear();
    }
}
