package cn.edu.nju.software.sda.core.domain.partition;

import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 代表着一个服务
 */
@Getter
@Setter
@ToString
public class PartitionNode{

    private String id;

    private String name;

    public PartitionNode(String name) {
        this.name = name;
    }

    private NodeSet nodeSet = new NodeSet();

    public void addNode(Node node){
        nodeSet.addNode(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PartitionNode that = (PartitionNode) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
