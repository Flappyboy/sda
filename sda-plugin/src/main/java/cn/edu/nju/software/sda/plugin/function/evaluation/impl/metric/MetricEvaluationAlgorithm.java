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

import java.util.*;

public class MetricEvaluationAlgorithm extends EvaluationAlgorithm {

    @Override
    public String getDesc() {
        return "SYS_Metric";
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
        //获取数据
        PartitionResult partitionResult = new PartitionResult(inputData);

        //稳定性指标
        double stability = getStability(partitionResult);
        //CHM
        double CHM = getCHM(partitionResult);
        //CHD
        double CHD = getCHD(partitionResult);
        //INF
        double INF = getINF(partitionResult);
        //OPN
        double OPN = getOPN(partitionResult);
        //IRN
        double IRN = getIRN(partitionResult);

        //返回结构
        Evaluation evaluation = new Evaluation();
        evaluation.addIndicator(new Indicator("stability", String.valueOf(stability)));
        evaluation.addIndicator(new Indicator("CHM", String.valueOf(CHM)));
        evaluation.addIndicator(new Indicator("CHD", String.valueOf(CHD)));
        evaluation.addIndicator(new Indicator("INF", String.valueOf(INF)));
        evaluation.addIndicator(new Indicator("OPN", String.valueOf(OPN)));
        evaluation.addIndicator(new Indicator("IRN", String.valueOf(IRN)));
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
        return "SYS_Metric";
    }

    //稳定性计算
    public double getStability(PartitionResult partitionResult){
        Map<String, PairRelation> edgeMap =partitionResult.edgeMap; //边key-边关系
        Map<String,String> nodePartitionMap =partitionResult.getNodePartitionMap(); //结点名称-社区
        double internalEdge = 0;
        double externalEdge = 0;
        for (Map.Entry<String, PairRelation> entry : edgeMap.entrySet()) {
            PairRelation edge = entry.getValue();
            String sourcePatition = nodePartitionMap.get(edge.getSourceNode().getName());
            String targetPatition = nodePartitionMap.get(edge.getTargetNode().getName());
            if(sourcePatition == targetPatition)
                internalEdge +=edge.getValue();
            else
                externalEdge +=edge.getValue();
        }
        System.out.println("内部："+internalEdge);
        System.out.println("外部："+externalEdge);
        double stability = internalEdge/(internalEdge+externalEdge);
        return stability;
    }

    //CHM
    public double getCHM(PartitionResult partitionResult){
        List<Community> communityList = partitionResult.getCommunityList();
        double CHMjSum = 0;
        double interfaceCount = 0;
        for(Community community :communityList){
            List<ClassNodeInfo> interfaces = community.getInterfaces();
            for(ClassNodeInfo classNodeInfo : interfaces){
                interfaceCount++;
                CHMjSum += classNodeInfo.calculateCHMi();
            }
        }
        return CHMjSum/interfaceCount;
    }
    //CHD
    public double getCHD(PartitionResult partitionResult){
        List<Community> communityList = partitionResult.getCommunityList();
        double CHDjSum = 0;
        double interfaceCount = 0;
        for(Community community :communityList){
            List<ClassNodeInfo> interfaces = community.getInterfaces();
            for(ClassNodeInfo classNodeInfo : interfaces){
                interfaceCount++;
                CHDjSum += classNodeInfo.calculateCHDi();
            }
        }
        return CHDjSum/interfaceCount;
    }
    //INF
    public double getINF(PartitionResult partitionResult){
        List<ClassNodeInfo> infs = partitionResult.getInfs();
        double k = partitionResult.getCommunityCount();
        return infs.size()/k;
    }
    //OPN
    public double getOPN(PartitionResult partitionResult){
        List<ClassNodeInfo> infs = partitionResult.getInfs();
        int sum= 0;
        for(ClassNodeInfo classNodeInfo:infs){
            sum += classNodeInfo.getOpCount();
        }
        return sum;
    }
    //IRN
    public int getIRN(PartitionResult partitionResult){
        Map<String ,PairRelation> edgeMap  = partitionResult.getEdgeMap();
        Map<String ,String> infNameCommunityIdMap = new HashMap<>();//infsName-->communityId
        List<Community> communityList = partitionResult.getCommunityList();
        for(Community community:communityList){
            List<ClassNodeInfo> interfaces = community.getInterfaces();
            for(ClassNodeInfo classNodeInfo:interfaces){
                infNameCommunityIdMap.put(classNodeInfo.getName(),community.getCommunityId());
            }
        }
        int num = 0;
        for (Map.Entry<String, PairRelation> entry : edgeMap.entrySet()) {
            PairRelation edge = entry.getValue();
            if(isContained(edge.getSourceNode().getName(),edge.getTargetNode().getName(),infNameCommunityIdMap)) {
                System.out.println(edge.toString());
                num += edge.getValue();
            }
        }
        System.out.println("num  :"+num);
        return num;
    }

    public static boolean isContained(String str1,String str2, Map<String ,String> infNameCommunityIdMap){
        if(infNameCommunityIdMap.containsKey(str1)&&infNameCommunityIdMap.containsKey(str2)){
            if(!infNameCommunityIdMap.get(str1).equals(infNameCommunityIdMap.get(str2)))
                return true;
            else
                return false;
        }else
            return false;
    }

}
