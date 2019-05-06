package cn.edu.nju.software.sda.app.entity.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PartitionGraph {
    private List<PartitionGraphNode> partitionNodes;
    private List<PartitionGraphEdge> partitionEdges;
}