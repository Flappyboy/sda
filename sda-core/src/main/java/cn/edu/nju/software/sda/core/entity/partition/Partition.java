package cn.edu.nju.software.sda.core.entity.partition;

import cn.edu.nju.software.sda.core.entity.node.Node;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Partition<N extends Node> {

    private String id;

    private String name;

    private Set<PartitionNode<N>> partitionNodeSet;
}
