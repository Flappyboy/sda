package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "partition_node_edge")
public class PartitionNodeEdgeEntity implements Serializable {

    @Id
    private String id;

    private String sourceId;

    private String targetId;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`desc`")
    private String desc;

    @Transient
    private List<PairRelationEntity> pairRelationList;

    @Transient
    private List<PartitionNodeEdgePairEntity> partitionNodeEdgePairEntities;
}