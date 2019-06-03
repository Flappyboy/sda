package cn.edu.nju.software.sda.plugin.function.evaluation.impl.metric;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import cn.edu.nju.software.sda.core.domain.info.PartitionInfo;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Setter
@Getter
@ToString
public class PartitionResult {
    private int communityCount;
    private List<Community> communityList = new ArrayList<>();//社区列表
    private Map<String, PairRelation> edgeMap = new HashMap<>();//边key-边关系
    private Map<String,String> nodePartitionMap = new HashMap<>() ;//结点名称-社区
    private List<ClassNodeInfo> infs = new ArrayList<>();

    public PartitionResult(InputData inputData) {
        //社区数据
        PartitionInfo partitionInfo = (PartitionInfo) inputData.getInfoDataObjs().get(Partition.INFO_NAME_PARTITION).get(0);
        Set<PartitionNode> partitionNodeSet = partitionInfo.getPartition().getPartitionNodeSet();
        //转换
        for(PartitionNode partitionNode:partitionNodeSet){
            Community community = new Community();
            community.setCommunityId(partitionNode.getId());
            List<ClassNodeInfo> interfaces = new ArrayList<>();
            List<ClassNodeInfo> allClasses = new ArrayList<>();
            NodeSet nodeSet = partitionNode.getNodeSet();
            for(Node node:nodeSet){
                this.nodePartitionMap.put(node.getName(),partitionNode.getId());
                ClassNodeInfo classNodeInfo = new ClassNodeInfo();
                classNodeInfo.setCommunityId(partitionNode.getId());
                classNodeInfo.setFullname(node.getName());
                classNodeInfo.setName(node.getName());
                if(node.getAttrStr().equals("INTERFACE")){
                    classNodeInfo.setFlag(1);//表示接口
                    List<MethodDesc> methodDescs = new ArrayList<>();
                    NodeSet childrenNodeSet = node.getChildrenNodeSet();
                    if(childrenNodeSet!=null) {
                        for (Node childNode : childrenNodeSet) {
                            String attrs[] = childNode.getAttrStr().split(";");
                            String methodName = attrs[1];
                            String retType = attrs[2];
                            String[] params = attrs[3].split(",");
                            MethodDesc methodDesc = new MethodDesc();
                            methodDesc.setMethodName(methodName);
                            methodDesc.setParam(Arrays.asList(params));
                            methodDesc.setParamCount(params.length);
                            if (retType.equals("V")) methodDesc.setRetCount(0);
                            methodDesc.setRetCount(1);
                            methodDesc.setRetType(retType);
                            methodDescs.add(methodDesc);
                        }

                        classNodeInfo.setOpCount(childrenNodeSet.size());
                    }else {
                        classNodeInfo.setOpCount(0);
                    }
                    classNodeInfo.setMethodDescs(methodDescs);
                    interfaces.add(classNodeInfo);
                }
                allClasses.add(classNodeInfo);
            }
            community.setAllClasses(allClasses);
            community.setInterfaces(interfaces);

            this.communityList.add(community);
        }
        //获取结点
        NodeInfo nodeInfo = inputData.getInfoSet().getInfoByClass(NodeInfo.class);
        NodeSet nodeSet = nodeInfo.getNodeSet();
        nodeSet.getSetByClass(MethodNode.class);
        nodeSet.getSetByClass(ClassNode.class);
        //获取类的静态调用关系
        PairRelationInfo pairRelationInfo = (PairRelationInfo)inputData.getInfoDataObjs().get(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT).get(0);
        for(PairRelation pairRelation:pairRelationInfo){
            Node  sourceNode = pairRelation.getSourceNode();
            Node targetNode = pairRelation.getTargetNode();
            String key = sourceNode.getName()+"-!-"+targetNode.getName();
            if (!this.edgeMap.containsKey(key)) {
                this.edgeMap.put(key, pairRelation);
            } else {
                PairRelation oldEdge = this.edgeMap.get(key);
                double oldCount = oldEdge.getValue();
                oldEdge.setValue(oldCount + pairRelation.getValue());
                this.edgeMap.put(key, oldEdge);
            }
        }
        this.communityCount = this.communityList.size();

        for(Community community:communityList){
            List<ClassNodeInfo> interfaces = community.getInterfaces();
            this.infs.addAll(interfaces);
        }

    }


}
