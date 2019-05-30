package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
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
import java.util.Map;

@Data
@NoArgsConstructor
@Table(name = "evaluation_info")
public class EvaluationInfoEntity implements Serializable {

    @Id
    private String id;

    private String partitionId;

    private String name;

    @Column(name = "`desc`")
    private String desc;

    private String status;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private List<EvaluationIndicatorEntity> indicators;

    public static EvaluationInfoEntity create(EvaluationInfo info){
        EvaluationInfoEntity entity = new EvaluationInfoEntity();
        entity.setId(info.getId());
        entity.setPartitionId(info.getParentId());
        entity.setStatus(info.getStatus().name());
        entity.setCreatedAt(info.getCreatedAt());
        entity.setUpdatedAt(info.getUpdatedAt());
        entity.setFlag(1);
        return entity;
    }

    public EvaluationInfo toEvaluationInfo(){
        EvaluationInfo info = new EvaluationInfo(new Evaluation());
        info.setId(this.id);
        info.setParentId(this.partitionId);
        info.setStatus(Info.InfoStatus.valueOf(this.getStatus()));
        info.setCreatedAt(this.createdAt);
        info.setUpdatedAt(this.updatedAt);
        info.setDesc(this.desc);
        return info;
    }

    public static List<EvaluationInfo> toEvaluationInfoList(List<EvaluationInfoEntity> entities){
        List<EvaluationInfo> infos = new ArrayList<>();
        for (EvaluationInfoEntity entity :
                entities) {
            infos.add(entity.toEvaluationInfo());
        }
        return infos;
    }
}