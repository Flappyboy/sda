package cn.edu.nju.software.sda.plugin.partition.impl.louvain;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.info.*;
import cn.edu.nju.software.sda.core.entity.node.ClassNode;
import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.node.NodeSet;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import cn.edu.nju.software.sda.core.entity.partition.PartitionNode;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.plugin.adapter.OrderKeyNodeSetAdapter;
import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LouvainPartitionPlugin implements PartitionPlugin {

    @Override
    public String getDesc() {
        return "Louvain 算法";
    }

    @Override
    public boolean match(EffectiveInfo effectiveInfo) {
        if(!ClassNode.class.equals(effectiveInfo.getTargetNodeClass())){
            return false;
        }

        boolean flag = false;
        for(Class clazz: effectiveInfo.getEffectiveNodeClass()){
            if(ClassNode.class.equals(clazz)){
                flag = true;
            }
        }
        if(!flag)
            return flag;
        flag = false;

        for (Info info :
                effectiveInfo.getEffectiveInfo()) {
            if(info instanceof RelationInfo){
                RelationInfo ri = (RelationInfo) info;
                if(PairRelation.class.equals(ri.getRelationClass()) && ClassNode.class.equals(ri.getNodeClass()) &&
                    ri.getName().equals(PairRelation.STATIC_CALL_COUNT)){
                    flag = true;
                }
            }
        }
        return flag;
    }

    @Override
    public Partition partition(App app, File workspace) throws IOException {

        NodeSet nodeSet = app.getNodeSet();

        InfoSet infoSet = app.getInfoSet();

        RelationInfo<PairRelation> staticRelationInfo = infoSet.getRelationInfo(PairRelation.class, Node.class, PairRelation.STATIC_CALL_COUNT);

        RelationInfo<PairRelation> dynamicRelationInfo = infoSet.getRelationInfo(PairRelation.class, Node.class, PairRelation.DYNAMIC_CALL_COUNT);

        OrderKeyNodeSetAdapter<Node> orderKeyNodeSet = new OrderKeyNodeSetAdapter<>(nodeSet, 1l);

        RelationInfo<PairRelation> allRelationInfo = new RelationInfo<>("all", Node.class, PairRelation.class);

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
        String edgePath = workspace.getAbsolutePath()+"/";
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

        FileUtil.writeFile(lines, filePath);
        String outputPath = edgePath + System.currentTimeMillis() + "_louvain.txt";
        try {
            LouvainUtil.execute(filePath, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Partition<Node> partition = new Partition<>();
        Set<PartitionNode<Node>> partitionNodeSet = new HashSet<>();
        partition.setPartitionNodeSet(partitionNodeSet);

        List<String> resultLines = FileUtil.readFile(outputPath);
        Integer communityCount = 0;
        for (int j = 1; j < resultLines.size(); j++) {
            String resultLine = resultLines.get(j);
            System.out.println(resultLine);
            communityCount += 1;

            PartitionNode<Node> partitionNode = new PartitionNode<>(communityCount.toString());
            partitionNodeSet.add(partitionNode);

            String[] communityKeys = resultLine.split(" ");
            for (int i = 0; i < communityKeys.length; i++) {
                Node node = orderKeyNodeSet.getNode(Long.valueOf(communityKeys[i]));
                partitionNode.addNode(node);
            }

        }
        return partition;
    }

    @Override
    public String getName() {
        return "SYS_Louvain_0.0.1";
    }
}
