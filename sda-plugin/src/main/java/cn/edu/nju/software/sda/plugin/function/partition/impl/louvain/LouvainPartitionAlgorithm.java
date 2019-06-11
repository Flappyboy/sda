package cn.edu.nju.software.sda.plugin.function.partition.impl.louvain;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.meta.MetaInfoDataItem;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.plugin.adapter.MergerPartition;
import cn.edu.nju.software.sda.plugin.adapter.NormalRelationAdaper;
import cn.edu.nju.software.sda.plugin.adapter.OrderKeyNodeSetAdapter;
import cn.edu.nju.software.sda.core.exception.WorkFailedException;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LouvainPartitionAlgorithm extends PartitionAlgorithm {

    @Override
    public String getDesc() {
        return "Louvain Algorithm";
    }

    @Override
    public MetaData getMetaData() {
        MetaData metaData = new MetaData();
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
    public InfoSet work(InputData inputData, Work work) throws WorkFailedException {

        NodeSet nodeSet = ((NodeInfo) inputData.getInfoSet().getInfoByName(Node.INFO_NAME_NODE)).getNodeSet();

        nodeSet = nodeSet.getNodeSet(ClassNode.class);

        InfoSet infoSet = inputData.getInfoSet();

        PairRelationInfo pri = (PairRelationInfo) infoSet.getInfoByName(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT);
//        RelationInfo<PairRelation> staticRelationInfo = pri==null? null: new NormalRelationAdaper(pri);
        RelationInfo<PairRelation> staticRelationInfo = pri;

        pri = (PairRelationInfo) infoSet.getInfoByName(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT);
//        RelationInfo<PairRelation> dynamicRelationInfo = pri==null? null: new NormalRelationAdaper(pri);
        RelationInfo<PairRelation> dynamicRelationInfo = pri;

        OrderKeyNodeSetAdapter orderKeyNodeSet = new OrderKeyNodeSetAdapter(nodeSet, 1l);

        PairRelationInfo allRelationInfo = new PairRelationInfo("all");

        if(staticRelationInfo != null){
            for(PairRelation pairRelation: staticRelationInfo){
                allRelationInfo.addRelationByAddValue(pairRelation);
            }
        }

        if(dynamicRelationInfo != null) {
            for (PairRelation pairRelation : dynamicRelationInfo) {
                allRelationInfo.addRelationByAddValue(pairRelation);
            }
        }
        //git
        GroupRelationInfo groupRelationInfo = (GroupRelationInfo) infoSet.getInfoByName(GroupRelation.GIT_COMMIT);
        if(groupRelationInfo != null){
            PairRelationInfo groupPairRelationInfo = groupRelationInfo.toPair(groupRelationInfo);
            for(PairRelation pairRelation: groupPairRelationInfo){
                allRelationInfo.addRelationByAddValue(pairRelation);
            }
        }

        //生成算法处理的输入文件
//        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String edgePath = work.getWorkspace().getAbsolutePath()+"/";
        String edgeFileName = System.currentTimeMillis() + "_edge.txt";
        FileUtil.creatFile(edgePath, edgeFileName);
        String filePath = edgePath + edgeFileName;

        File file = new File("");

        List<String> lines = new ArrayList<>();
        String count = (nodeSet.size() + 1) + " " + allRelationInfo.size();
        lines.add(count);

        for(PairRelation pr: allRelationInfo){
            String line = orderKeyNodeSet.getKey(pr.getSourceNode()) + " "
                    + orderKeyNodeSet.getKey(pr.getTargetNode()) + " "
                    + pr.getValue();
            lines.add(line);
        }

        try {
            FileUtil.writeFile(lines, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputPath = edgePath + System.currentTimeMillis() + "_louvain.txt";
        try {
            LouvainUtil.execute(filePath, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Partition partition = new Partition();
        Set<PartitionNode> partitionNodeSet = new HashSet<>();
        partition.setPartitionNodeSet(partitionNodeSet);

        List<String> resultLines = null;
        try {
            resultLines = FileUtil.readFile(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer communityCount = 0;
        for (int j = 1; j < resultLines.size(); j++) {
            String resultLine = resultLines.get(j);
            System.out.println(resultLine);
            communityCount += 1;

            PartitionNode partitionNode = new PartitionNode(communityCount.toString());
            partitionNodeSet.add(partitionNode);

            String[] communityKeys = resultLine.split(" ");
            for (int i = 0; i < communityKeys.length; i++) {
                Node node = orderKeyNodeSet.getNode(Long.valueOf(communityKeys[i]));
                partitionNode.addNode(node);
            }

        }
        Info info = new PartitionInfo(new MergerPartition(partition, allRelationInfo));
        String desc = "Louvain";
        if(staticRelationInfo!=null)
            desc += " static";
        if(dynamicRelationInfo!=null){
            desc += " dynamic";
        }
        info.setDesc(desc);
        return new InfoSet(info);
    }

    @Override
    public String getName() {
        return "SYS_Louvain_0.0.1";
    }
}
