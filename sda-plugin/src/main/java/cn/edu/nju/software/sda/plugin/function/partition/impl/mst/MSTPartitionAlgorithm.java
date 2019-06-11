package cn.edu.nju.software.sda.plugin.function.partition.impl.mst;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.meta.FormDataType;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaFormDataItem;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.plugin.adapter.MergerPartition;
import cn.edu.nju.software.sda.plugin.adapter.OrderKeyNodeSetAdapter;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.GitCommitFileEdge;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;
import cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util.Component;
import cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util.MST;
import cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util.MSTCluster;

import java.util.*;

public class MSTPartitionAlgorithm extends PartitionAlgorithm {

    @Override
    public String getDesc() {
        return "MST Algorithm";
    }

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
        metaData.addMetaDataItem(new MetaFormDataItem("SplitThreshold", FormDataType.STRING));
        metaData.addMetaDataItem(new MetaFormDataItem("NumServices", FormDataType.STRING));
        metaData.addMetaDataItem(new MetaInfoDataItem(Node.INFO_NAME_NODE));
        metaData.addMetaDataItem(new MetaInfoDataItem(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT));
        metaData.addMetaDataItem(new MetaInfoDataItem(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT));
        metaData.addMetaDataItem(new MetaInfoDataItem(GroupRelation.GIT_COMMIT));
        return metaData;
    }

    @Override
    public ResultDto check(InputData inputData) {
        return ResultDto.ok();
    }

    @Override
    public InfoSet work(InputData inputData, Work work) {
        System.out.println("----------ha-----------------");
        NodeSet nodeSet = ((NodeInfo) inputData.getInfoSet().getInfoByName(Node.INFO_NAME_NODE)).getNodeSet();
        nodeSet = nodeSet.getNodeSet(ClassNode.class);
        InfoSet infoSet = inputData.getInfoSet();
        PairRelationInfo staticRelationInfo = (PairRelationInfo) infoSet.getInfoByName(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT);
        PairRelationInfo dynamicRelationInfo = (PairRelationInfo) infoSet.getInfoByName(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT);
        GroupRelationInfo groupRelationInfo = (GroupRelationInfo) infoSet.getInfoByName(GroupRelation.GIT_COMMIT);
        PairRelationInfo allRelationInfo = new PairRelationInfo("all");
        //静态
        if(staticRelationInfo != null){
            for(PairRelation pairRelation: staticRelationInfo){
                allRelationInfo.addRelationByAddValue(pairRelation);
            }
        }
        //动态
        if(dynamicRelationInfo != null) {
            for (PairRelation pairRelation : dynamicRelationInfo) {
                allRelationInfo.addRelationByAddValue(pairRelation);
            }
        }
        //git
        if(groupRelationInfo != null){
            PairRelationInfo groupPairRelationInfo = groupRelationInfo.toPair(groupRelationInfo);
            for(PairRelation pairRelation: groupPairRelationInfo){
                allRelationInfo.addRelationByAddValue(pairRelation);
            }
        }

        List<GitCommitFileEdge> sEdges = new ArrayList<>();
        for(PairRelation relation :allRelationInfo){
            GitCommitFileEdge edge=new GitCommitFileEdge(relation);
            sEdges.add(edge);
        }

        Partition partition = new Partition();
        Set<PartitionNode> partitionNodeSet = new HashSet<>();
        partition.setPartitionNodeSet(partitionNodeSet);

        List<Object> splitThresholdObj =inputData.getFormDataObjs(getMetaData()).get("SplitThreshold");
        int splitThreshold = Integer.valueOf((String)splitThresholdObj.get(0));
        List<Object> numServicesObj =inputData.getFormDataObjs(getMetaData()).get("NumServices");
        int numServices = Integer.valueOf((String)numServicesObj.get(0));
        //MSTCluster
//        Set<Component> components = new HashSet<>(MSTCluster.clusterWithSplit(MST.calcMST(sEdges), 100,3));
        Set<Component> components = new HashSet<>(MSTCluster.clusterWithSplit(MST.calcMST(sEdges), splitThreshold,numServices));
        System.out.println("components.size = " + components.size());
        Integer communityCount = 0;
        for (Component cpt : components){
            System.out.println("*******************************one components "+ cpt.getNodes().size() +"******************************");
            PartitionNode partitionNode = new PartitionNode(communityCount.toString());
            NodeSet partitionNodes = new NodeSet();
            for (cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util.ClassNode node: cpt.getNodes()){
                System.out.println(node.getClassName());
                partitionNodes.addNode(nodeSet.getNodeByName(node.getClassName()));
            }
            partitionNode.setNodeSet(partitionNodes);
            partitionNodeSet.add(partitionNode);
            communityCount++;
        }

        Info info = new PartitionInfo(partition);
        String desc = "MST";

        desc += " "+splitThreshold + "-" + numServices;

        if(staticRelationInfo!=null)
            desc += " static";
        if(dynamicRelationInfo!=null){
            desc += " dynamic";
        }
        if(groupRelationInfo!=null){
            desc += " git";
        }
        info.setDesc(desc);
        return new InfoSet(info);
    }


    @Override
    public String getName() {
        return "SYS_MST_0.0.1";
    }
}
