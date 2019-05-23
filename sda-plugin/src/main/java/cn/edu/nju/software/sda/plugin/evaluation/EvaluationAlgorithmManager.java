package cn.edu.nju.software.sda.plugin.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationAlgorithmManager {

    private static Map<String, EvaluationPlugin> evaluationPluginMap = new HashMap<>();

    public static void register(EvaluationPlugin evaluationPlugin){
        evaluationPluginMap.put(evaluationPlugin.getName(), evaluationPlugin);
    }

    public static EvaluationPlugin get(String name){
        return evaluationPluginMap.get(name);
    }

    public static List<EvaluationPlugin> get(){
        return new ArrayList<>(evaluationPluginMap.values());
    }

    public static void clear(){
        evaluationPluginMap.clear();
    }
}
