package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.node.NodeSet;
import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;

public class GroupRelation extends Relation {

    public static final String GIT_COMMIT = "GIT_COMMIT";

    private NodeSet<Node> nodeSet = new NodeSet<>();

    public GroupRelation(String id, Double value, Class nodeClass) {
        super(id, value, nodeClass);
    }

    public void addNode(Node node){
        if(node==null)
            throw new NullPointerException();
        if (!getNodeClass().isAssignableFrom(node.getClass()))
            throw new UnexpectedClassException(getNodeClass(), node.getClass());
    }

    public NodeSet<Node> getNodeSetCopy(){
        return new NodeSet<>().addNode(nodeSet);
    }
}
