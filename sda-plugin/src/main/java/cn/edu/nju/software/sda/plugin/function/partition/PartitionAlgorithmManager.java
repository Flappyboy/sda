package cn.edu.nju.software.sda.plugin.function.partition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartitionAlgorithmManager {

    private static Map<String, PartitionAlgorithm> partitioPluginMap = new HashMap<>();

    public static void register(PartitionAlgorithm partitionAlgorithm){
        partitioPluginMap.put(partitionAlgorithm.getName(), partitionAlgorithm);
    }

    public static PartitionAlgorithm get(String name){
        return partitioPluginMap.get(name);
    }

    public static List<PartitionAlgorithm> get(){
        return new ArrayList<>(partitioPluginMap.values());
    }

    public static void clear(){
        partitioPluginMap.clear();
    }
}
