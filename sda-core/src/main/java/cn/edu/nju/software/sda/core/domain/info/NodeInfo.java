package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NodeInfo extends Info {
    private NodeSet nodeSet;

    public NodeInfo(NodeSet nodeSet) {
        super(Node.INFO_NAME_NODE);
        this.nodeSet = nodeSet;
    }
}
