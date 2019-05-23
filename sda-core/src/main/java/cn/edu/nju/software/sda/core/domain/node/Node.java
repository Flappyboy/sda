package cn.edu.nju.software.sda.core.domain.node;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@ToString
public abstract class Node {
    public static final String INFO_NAME_NODE = "SYS_NODE";

    private String id;

    private String name;

    private Node parentNode;

//    private NodeSet childrenNodeSet;

    public Node(String name) {
        this.name = name;
    }

    public abstract String getAttrStr();

    public abstract void setAttrStr(String attrStr);

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
