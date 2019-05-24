package cn.edu.nju.software.sda.plugin.function.evaluation;

import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;

public abstract class EvaluationAlgorithm implements FunctionService {

    @Override
    public FunctionType getType() {
        return FunctionType.Evaluation;
    }
}
