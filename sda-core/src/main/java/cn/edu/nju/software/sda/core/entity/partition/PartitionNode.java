package cn.edu.nju.software.sda.core.entity.partition;

import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.node.NodeSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 代表着一个服务
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PartitionNode<N extends Node>{

    private String id;

    @EqualsAndHashCode.Include
    private String name;

    private NodeSet<N> nodeSet;
}
