package cn.edu.nju.software.sda.plugin.function.evaluation.impl.demo;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import cn.edu.nju.software.sda.core.domain.evaluation.Indicator;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.plugin.function.evaluation.EvaluationAlgorithm;

public class DemoEvaluationAlgorithm extends EvaluationAlgorithm {

    @Override
    public String getDesc() {
        return "Demo";
    }

    @Override
    public MetaData evaluationMetaData(MetaData metaData) {
        metaData.addMetaDataItem(new MetaInfoDataItem(Node.INFO_NAME_NODE));
        metaData.addMetaDataItem(new MetaInfoDataItem(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT));
        return metaData;
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EvaluationInfo evaluationInfo = new EvaluationInfo(evaluation);
        return new InfoSet(evaluationInfo);
    }

    @Override
    public String getName() {
        return "SYS_Demo";
    }


}
