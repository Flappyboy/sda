package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PairRelation extends Relation {

    public static final String INFO_NAME_STATIC_CLASS_CALL_COUNT = "SYS_RELATION_PAIR_STATIC_ClASS_CALL_COUNT";
    public static final String INFO_NAME_STATIC_METHOD_CALL_COUNT = "SYS_RELATION_PAIR_STATIC_METHOD_CALL_COUNT";
    public static final String INFO_NAME_DYNAMIC_CLASS_CALL_COUNT = "SYS_RELATION_PAIR_DYNAMIC_CLASS_CALL_COUNT";
    public static final String INFO_NAME_DYNAMIC_METHOD_CALL_COUNT = "SYS_RELATION_PAIR_DYNAMIC_METHOD_CALL_COUNT";

    public PairRelation(String id, Double value, Node sourceNode, Node targetNode) {
        super(id, value);
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }
    public PairRelation(Double value, Node sourceNode, Node targetNode) {
        super(value);
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }

    private Node sourceNode;

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode){
        if(sourceNode==null)
            throw new NullPointerException();
        if (!Node.class.isAssignableFrom(sourceNode.getClass()))
            throw new UnexpectedClassException(Node.class, sourceNode.getClass());

        this.sourceNode = sourceNode;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode){
        if(targetNode==null)
            throw new NullPointerException();
        if (!Node.class.isAssignableFrom(targetNode.getClass()))
            throw new UnexpectedClassException(Node.class, targetNode.getClass());

        this.targetNode = targetNode;
    }

    private Node targetNode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

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
