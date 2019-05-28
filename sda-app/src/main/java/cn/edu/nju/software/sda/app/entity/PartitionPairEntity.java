package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Table(name = "partition_pair")
public class PartitionPairEntity implements Serializable {

    @Id
    private String id;

    private String partitionInfoId;

    private String pairRelationInfoId;
}