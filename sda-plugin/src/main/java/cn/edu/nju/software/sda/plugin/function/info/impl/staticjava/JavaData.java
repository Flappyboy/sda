package cn.edu.nju.software.sda.plugin.function.info.impl.staticjava;

import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class JavaData {
    NodeSet nodeSet = new NodeSet();

    PairRelationInfo classEdges = new PairRelationInfo(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT);

    PairRelationInfo methodEdges = new PairRelationInfo(PairRelation.INFO_NAME_STATIC_METHOD_CALL_COUNT);

    public List<Info> getInfos(){
        clearUp();
        List<Info> infoList = new ArrayList<>();
        NodeInfo nodeInfo = new NodeInfo(nodeSet);
        infoList.add(nodeInfo);
        infoList.add(classEdges);
        infoList.add(methodEdges);
        return infoList;
    }

    /**
     * edges中两端必须都为nodeSet中节点，否则删除
     */
    private void clearUp(){
        // TODO
    }
}
