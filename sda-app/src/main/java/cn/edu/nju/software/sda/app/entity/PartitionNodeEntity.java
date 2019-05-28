package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "partition_node")
public class PartitionNodeEntity implements Serializable {

    @Id
    private String id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`desc`")
    private String desc;

    private String partitionId;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    public static PartitionNodeEntity create(String partitionId, PartitionNode partitionNode){
        PartitionNodeEntity entity = new PartitionNodeEntity();

        entity.setId(partitionNode.getId());
        entity.setName(partitionNode.getName());
        entity.setPartitionId(partitionId);
        entity.setFlag(1);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());

        return entity;
    }
}