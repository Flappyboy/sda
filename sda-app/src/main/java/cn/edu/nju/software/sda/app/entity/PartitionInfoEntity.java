package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.info.PartitionInfo;
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
@Table(name = "partition_info")
public class PartitionInfoEntity implements Serializable {

    @Id
    private String id;

    private String appId;

    @Column(name = "`desc`")
    private String desc;

    private String status;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private String appName;

    @Transient
    private List<PairRelationInfoEntity> pairRelationInfoEntityList;

    public static PartitionInfoEntity create(PartitionInfo partitionInfo){
        PartitionInfoEntity entity = new PartitionInfoEntity();
        entity.setId(partitionInfo.getId());
        entity.setAppId(partitionInfo.getParentId());
        entity.setStatus(partitionInfo.getStatus().name());
        entity.setCreatedAt(partitionInfo.getCreatedAt());
        entity.setUpdatedAt(partitionInfo.getUpdatedAt());
        entity.setFlag(1);
        return entity;
    }
}