package cn.edu.nju.software.sda.plugin.function.evaluation.impl.demo;

import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.Indicator;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.plugin.function.evaluation.EvaluationAlgorithm;

public class DemoEvaluationAlgorithm extends EvaluationAlgorithm {

    @Override
    public String getDesc() {
        return "Demo";
    }

    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) {
        Evaluation evaluation = new Evaluation();
        evaluation.addIndicator(new Indicator("indicator1", "12"));
        evaluation.addIndicator(new Indicator("indicator2", "16"));
        return new InfoSet();
    }

    @Override
    public String getName() {
        return "SYS_Demo";
    }
}
