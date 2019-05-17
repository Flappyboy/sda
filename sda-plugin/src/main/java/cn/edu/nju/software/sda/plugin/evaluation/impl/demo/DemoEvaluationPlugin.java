package cn.edu.nju.software.sda.plugin.evaluation.impl.demo;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.entity.evaluation.Indicator;
import cn.edu.nju.software.sda.plugin.evaluation.EvaluationPlugin;

import java.io.File;
import java.io.IOException;

public class DemoEvaluationPlugin implements EvaluationPlugin {

    @Override
    public String getDesc() {
        return "Demo";
    }

    @Override
    public boolean match(EffectiveInfo effectiveInfo) {
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
