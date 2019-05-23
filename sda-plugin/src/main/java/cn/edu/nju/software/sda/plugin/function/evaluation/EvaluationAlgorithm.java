package cn.edu.nju.software.sda.plugin.function.evaluation;

import cn.edu.nju.software.sda.plugin.function.FunctionType;
import cn.edu.nju.software.sda.plugin.function.PluginFunction;

public abstract class EvaluationAlgorithm implements PluginFunction {

    @Override
    public FunctionType getType() {
        return FunctionType.Evaluation;
    }
}
