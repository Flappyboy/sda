package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.PartitionInfo;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Transient
    private EvaluationInfoEntity lastEvaluation;

    @Transient
    private TaskEntity taskEntity;

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

    public PartitionInfo toPartitionInfo(){
        PartitionInfo info = new PartitionInfo(new Partition());
        info.setId(this.id);
        info.setParentId(this.appId);
        info.setStatus(Info.InfoStatus.valueOf(this.getStatus()));
        info.setCreatedAt(this.createdAt);
        info.setUpdatedAt(this.updatedAt);
        info.setDesc(this.desc);
        return info;
    }

    public static List<PartitionInfo> toPartitionInfoList(List<PartitionInfoEntity> entities){
        List<PartitionInfo> infos = new ArrayList<>();
        for (PartitionInfoEntity entity :
                entities) {
            infos.add(entity.toPartitionInfo());
        }
        return infos;
    }
}