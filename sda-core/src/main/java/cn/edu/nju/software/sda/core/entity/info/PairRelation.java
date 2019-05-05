package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;

import java.rmi.UnexpectedException;

public class PairRelation extends Relation {

    public PairRelation(String id, String name, Long value, Class nodeClass) {
        super(id, name, value, nodeClass);
    }

    public static enum Direction {
        FORWORD("Forword"),
        REVERSE("reverse"),
        NONE("None")
        ;

        private String name;

        Direction(String name) {
            this.name = name;
        }
    }

    private Node sourceNode;

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode){
        if(sourceNode==null)
            throw new NullPointerException();
        if (!getNodeClass().equals(sourceNode.getClass()))
            throw new UnexpectedClassException(getNodeClass(), sourceNode.getClass());

        this.sourceNode = sourceNode;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode){
        if(targetNode==null)
            throw new NullPointerException();
        if (!getNodeClass().equals(targetNode.getClass()))
            throw new UnexpectedClassException(getNodeClass(), targetNode.getClass());

        this.targetNode = targetNode;
    }

    private Node targetNode;
}
