package cn.edu.nju.software.sda.core.entity.node;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@ToString
public abstract class Node {
    private String id;

    private String name;

    private Type type;

    public Node(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    /**
     * name相同即相同
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        Node node = (Node) o;

        return new EqualsBuilder()
                .append(name, node.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
