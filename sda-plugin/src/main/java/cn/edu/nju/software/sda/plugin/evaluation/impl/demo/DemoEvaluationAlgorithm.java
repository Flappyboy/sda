package cn.edu.nju.software.sda.plugin.evaluation.impl.demo;

import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.Indicator;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.plugin.evaluation.EvaluationAlgorithm;

import java.io.File;
import java.io.IOException;

public class DemoEvaluationAlgorithm implements EvaluationAlgorithm {

    @Override
    public String getDesc() {
        return "Demo";
    }

    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public boolean match(InputData inputData) {
        return true;
    }

    @Override
    public Evaluation evaluate(App app, File workspace) throws IOException {
        Evaluation evaluation = new Evaluation();
        evaluation.addIndicator(new Indicator("indicator1", "12"));
        evaluation.addIndicator(new Indicator("indicator2", "16"));
        return evaluation;
    }

    @Override
    public String getName() {
        return "SYS_Demo";
    }
}
