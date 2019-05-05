package cn.edu.nju.software.sda.core.entity.info;

import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.rmi.UnexpectedException;

public class PairRelation extends Relation {

    public static final String STATIC_CALL_COUNT = "STATIC_CALL_COUNT";
    public static final String DYNAMIC_CALL_COUNT = "DYNAMIC_CALL_COUNT";

    public PairRelation(String id, Double value, Class nodeClass) {
        super(id, value, nodeClass);
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
        if (!getNodeClass().isAssignableFrom(sourceNode.getClass()))
            throw new UnexpectedClassException(getNodeClass(), sourceNode.getClass());

        this.sourceNode = sourceNode;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode){
        if(targetNode==null)
            throw new NullPointerException();
        if (!getNodeClass().isAssignableFrom(targetNode.getClass()))
            throw new UnexpectedClassException(getNodeClass(), targetNode.getClass());

        this.targetNode = targetNode;
    }

    private Node targetNode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PairRelation that = (PairRelation) o;

        return new EqualsBuilder()
                .append(sourceNode, that.sourceNode)
                .append(targetNode, that.targetNode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sourceNode)
                .append(targetNode)
                .toHashCode();
    }
}
