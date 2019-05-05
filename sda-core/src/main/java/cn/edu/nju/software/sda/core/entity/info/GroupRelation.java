package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.node.NodeSet;
import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;

public class GroupRelation extends Relation {

    private NodeSet<Node> nodeSet = new NodeSet<>();

    public GroupRelation(String id, String name, Long value, Class nodeClass) {
        super(id, name, value, nodeClass);
    }

    public void addNode(Node node){
        if(node==null)
            throw new NullPointerException();
        if (!getNodeClass().equals(node.getClass()))
            throw new UnexpectedClassException(getNodeClass(), node.getClass());
    }

    public NodeSet<Node> getNodeSetCopy(){
        return new NodeSet<>().addNode(nodeSet);
    }
}
