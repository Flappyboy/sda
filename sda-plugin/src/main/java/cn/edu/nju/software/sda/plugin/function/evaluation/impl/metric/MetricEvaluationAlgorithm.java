package cn.edu.nju.software.sda.plugin.function.evaluation.impl.metric;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import cn.edu.nju.software.sda.core.domain.evaluation.Indicator;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaFormDataItem;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.plugin.function.evaluation.EvaluationAlgorithm;

import java.util.List;
import java.util.Set;

public class MetricEvaluationAlgorithm extends EvaluationAlgorithm {

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
//        获取数据
        PartitionInfo partitionInfo = (PartitionInfo) inputData.getInfoDataObjs().get(Partition.INFO_NAME_PARTITION).get(0);
        Set<PartitionNode> partitionNodeSet = partitionInfo.getPartition().getPartitionNodeSet();

        NodeInfo nodeInfo = inputData.getInfoSet().getInfoByClass(NodeInfo.class);
        NodeSet nodeSet = nodeInfo.getNodeSet();
        nodeSet.getSetByClass(MethodNode.class);
        nodeSet.getSetByClass(ClassNode.class);
        List<Info> pairRelationInfo = inputData.getInfoDataObjs().get(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT);

//        稳定性指标
//        CHM
//        CHD
//        INF
//        OPN
//        IRN

//        返回结构
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
