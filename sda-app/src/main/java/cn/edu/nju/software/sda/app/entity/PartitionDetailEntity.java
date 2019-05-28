package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "partition_detail")
public class PartitionDetailEntity implements Serializable {

    @Id
    private String id;

    private String nodeId;

    private String partitionNodeId;

    private Integer flag;

    @Column(name = "`desc`")
    private String desc;

    private Date createdAt;

    private Date updatedAt;
}