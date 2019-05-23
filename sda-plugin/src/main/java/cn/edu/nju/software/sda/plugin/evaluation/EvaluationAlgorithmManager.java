package cn.edu.nju.software.sda.plugin.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationAlgorithmManager {

    private static Map<String, EvaluationAlgorithm> evaluationPluginMap = new HashMap<>();

    public static void register(EvaluationAlgorithm evaluationAlgorithm){
        evaluationPluginMap.put(evaluationAlgorithm.getName(), evaluationAlgorithm);
    }

    public static EvaluationAlgorithm get(String name){
        return evaluationPluginMap.get(name);
    }

    public static List<EvaluationAlgorithm> get(){
        return new ArrayList<>(evaluationPluginMap.values());
    }

    public static void clear(){
        evaluationPluginMap.clear();
    }
}