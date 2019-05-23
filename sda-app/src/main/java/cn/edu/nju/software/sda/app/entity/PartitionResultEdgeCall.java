package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class PartitionResultEdgeCall implements Serializable {

    @Id
    private String id;

    private String edgeid;

    private String callid;

    private PairRelationEntity call;

    private Integer calltype;

    private Date createdat;

    private Date updatedat;
}