package cn.edu.nju.software.sda.core.domain.info;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PairRelationInfo extends RelationInfo<PairRelation> {
    public PairRelationInfo(String name) {
        super(name);
    }

    public List<PairRelation> findPairRelationByNodeName(String nodeName){
        List<PairRelation> list = new ArrayList<>();
        if(StringUtils.isBlank(nodeName)){
            return list;
        }
        for (PairRelation pr :
                this) {
            if (nodeName.equals(pr.getSourceNode().getName()) || nodeName.equals(pr.getTargetNode().getName())) {
                if(!pr.getSourceNode().equals(pr.getTargetNode()))
                    list.add(pr);
            }
        }
        return list;
    }
}
