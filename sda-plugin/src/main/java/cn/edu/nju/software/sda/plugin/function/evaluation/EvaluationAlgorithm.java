package cn.edu.nju.software.sda.plugin.function.evaluation;

import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;

public abstract class EvaluationAlgorithm implements FunctionService {

    @Override
    public FunctionType getType() {
        return FunctionType.Evaluation;
    }

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
        metaData.addMetaDataItem(new MetaInfoDataItem(Partition.INFO_NAME_PARTITION));
        return evaluationMetaData(metaData);
    }

    public abstract MetaData evaluationMetaData(MetaData metaData);
}
