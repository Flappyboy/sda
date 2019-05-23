package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "partition_node_edge_call")
public class PartitionNodeEdgeCallEntity implements Serializable {

    @Id
    private String id;

    private String edgeid;

    private String callid;

    private PairRelationEntity call;

    private Integer calltype;

    private Date createdat;

    private Date updatedat;
}