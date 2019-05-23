package cn.edu.nju.software.sda.plugin.partition;

import cn.edu.nju.software.sda.plugin.evaluation.EvaluationPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartitionAlgorithmManager {

    private static Map<String, PartitionPlugin> partitioPluginMap = new HashMap<>();

    public static void register(PartitionPlugin partitionPlugin){
        partitioPluginMap.put(partitionPlugin.getName(), partitionPlugin);
    }

    public static PartitionPlugin get(String name){
        return partitioPluginMap.get(name);
    }

    public static List<PartitionPlugin> get(){
        return new ArrayList<>(partitioPluginMap.values());
    }

    public static void clear(){
        partitioPluginMap.clear();
    }
}
