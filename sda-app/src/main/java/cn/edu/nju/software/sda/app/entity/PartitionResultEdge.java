package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PartitionResultEdge implements Serializable {

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

    private List<PartitionResultEdgeCall> partitionResultEdgeCallList;

    public PartitionResultEdge(String id, String patitionResultAId, String patitionResultBId, String name, String desc, Date createdAt, Date updatedAt) {
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