package cn.edu.nju.software.sda.core.entity.partition;

import cn.edu.nju.software.sda.core.entity.node.Node;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class Partition<N extends Node> {

    private String id;

    private String name;

    private Set<PartitionNode<N>> partitionNodeSet;
}
