package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;

public class GroupRelationInfo extends RelationInfo<GroupRelation> {
    public GroupRelationInfo(String name) {
        super(name);
    }

    public PairRelationInfo toPair(GroupRelationInfo groupRelationInfo){
        PairRelationInfo pairRelationInfo = new PairRelationInfo("group");
        for(GroupRelation groupRelation:groupRelationInfo){
            NodeSet nodeSet = groupRelation.getNodeSetCopy();
            for(Node node1 :nodeSet){
                for(Node node2 :nodeSet){
                    if(node1.getName().equals(node2.getName())){
                        PairRelation pairRelation1 = new PairRelation(1.0,node1,node2);
                        PairRelation pairRelation2 = new PairRelation(1.0,node2,node1);
                        pairRelationInfo.addRelationByAddValue(pairRelation1);
                        pairRelationInfo.addRelationByAddValue(pairRelation2);
                    }
                }

            }
        }
        return pairRelationInfo;
    }

}
