package cn.edu.nju.software.sda.plugin.function.partition.impl.louvain;

import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.plugin.adapter.OrderKeyNodeSetAdapter;
import cn.edu.nju.software.sda.plugin.exception.WorkFailedException;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LouvainPartitionAlgorithm extends PartitionAlgorithm {

    @Override
    public String getDesc() {
        return "Louvain 算法";
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
    public InfoSet work(InputData inputData, Work work) throws WorkFailedException {

        NodeSet nodeSet = ((NodeInfo) inputData.getInfoSet().getInfoByName(Node.INFO_NAME_NODE)).getNodeSet();

        InfoSet infoSet = inputData.getInfoSet();

        PairRelationInfo staticRelationInfo = (PairRelationInfo) infoSet.getInfoByName(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT);

        PairRelationInfo dynamicRelationInfo = (PairRelationInfo) infoSet.getInfoByName(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT);

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

        return new InfoSet(new PartitionInfo(partition));
    }

    @Override
    public String getName() {
        return "SYS_Louvain_0.0.1";
    }
}
