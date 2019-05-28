package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Table(name = "partition_node_edge_pair")
public class PartitionNodeEdgePairEntity implements Serializable {

    @Id
    private String id;

    private String edgeId;

    private String callId;

    private PairRelationEntity pair;
}