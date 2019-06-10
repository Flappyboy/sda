package cn.edu.nju.software.sda.plugin.adapter;

import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 将一些小的独立的partitionNode合并
 */
public class MergerPartition extends Partition {
    private PairRelationInfo pairRelations;
    public MergerPartition(Partition partition, PairRelationInfo pairRelations) {
        super();
        this.pairRelations = pairRelations;
        setId(partition.getId());
        setName(partition.getName());
        setPartitionNodeSet(partition.getPartitionNodeSet());
    }

    @Override
    public void setPartitionNodeSet(Set<PartitionNode> partitionNodeSet) {
        Set<PartitionNode> targetPN = new HashSet<>();
        Set<PartitionNode> result = new HashSet<>();
        for (PartitionNode pn :
                partitionNodeSet) {
            NodeSet ns = pn.getNodeSet();
            if(ns.size()==1){
                Node node = ns.iterator().next();
                List list = pairRelations.findPairRelationByNodeName(node.getName());
                if(list.size()==0){
                    targetPN.add(pn);
                    continue;
                }
            }
            result.add(pn);
        }
        if(targetPN.size()>0) {
            NodeSet nodeSet = new NodeSet();
            for (PartitionNode pn :
                    targetPN) {
                nodeSet.addNode(pn.getNodeSet());
            }
            PartitionNode partitionNode = targetPN.iterator().next();
            partitionNode.setNodeSet(nodeSet);
            result.add(partitionNode);
        }

        super.setPartitionNodeSet(result);
    }
}
