package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "partition_node_edge")
public class PartitionNodeEdgeEntity implements Serializable {

    @Id
    private String id;

    private String patitionResultAId;

    private String patitionResultBId;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`desc`")
    private String desc;

    private Date createdAt;

    private Date updatedAt;

    private List<PairRelationEntity> staticCallInfoList;

    private List<PairRelationEntity> dynamicCallInfoList;

    private List<PartitionNodeEdgeCallEntity> partitionNodeEdgeCallEntityList;

    public PartitionNodeEdgeEntity(String id, String patitionResultAId, String patitionResultBId, String name, String desc, Date createdAt, Date updatedAt) {
        this.id = id;
        this.patitionResultAId = patitionResultAId;
        this.patitionResultBId = patitionResultBId;
        this.name = name;
        this.desc = desc;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean isCaller(String partitionResultId){
        if(partitionResultId == null){
            throw new RuntimeException("partitionResultId is NULL");
        }
        return partitionResultId.equals(patitionResultAId);
    }
}