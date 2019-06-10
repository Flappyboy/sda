package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.exception.UnexpectedClassException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GroupRelation extends Relation {

    public static final String GIT_COMMIT = "SYS_RELATION_GROUP_GIT_COMMIT";

    private NodeSet nodeSet = new NodeSet();

    public NodeSet getNodeSet() {
        return nodeSet;
    }

    public GroupRelation(String id, Double value) {
        super(id, value);
    }

    @Override
    public Relation clone() {
        GroupRelation groupRelation = new GroupRelation(this.getId(), this.getValue());
        groupRelation.nodeSet = getNodeSetCopy();
        return groupRelation;
    }

    public GroupRelation(Double value) {
        super(value);
    }

    public void addNode(Node node){
        if(node==null)
            throw new NullPointerException();
        if (!Node.class.isAssignableFrom(node.getClass()))
            throw new UnexpectedClassException(Node.class, node.getClass());
        this.nodeSet.addNode(node);
    }

    public NodeSet getNodeSetCopy(){
        return new NodeSet().addNode(nodeSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GroupRelation that = (GroupRelation) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(nodeSet, that.nodeSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(nodeSet)
                .toHashCode();
    }
}
