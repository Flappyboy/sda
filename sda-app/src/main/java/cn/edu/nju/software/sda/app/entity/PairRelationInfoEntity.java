package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
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
@Table(name = "pair_relation_info")
public class PairRelationInfoEntity implements Serializable {

    @Id
    String id;

    String appId;

    String name;

    String type;

    @Column(name = "`desc`")
    String desc;

    Integer status;

    Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private String appName;

    @Transient
    List<PairRelationEntity> pairRelationEntityList;

    public static PairRelationInfoEntity createNewEntity(PairRelationInfo pairRelationInfo){
        PairRelationInfoEntity pairRelationInfoEntity = new PairRelationInfoEntity();
        pairRelationInfoEntity.setId(pairRelationInfo.getId());
        pairRelationInfoEntity.setAppId(pairRelationInfo.getParentId());
        pairRelationInfoEntity.setName(pairRelationInfo.getName());
        pairRelationInfoEntity.setDesc(pairRelationInfo.getDesc());
        pairRelationInfoEntity.setFlag(1);
        pairRelationInfoEntity.setStatus(Info.InfoStatus.COMPLETE.equals(pairRelationInfo.getStatus())? 1 : 0);
        pairRelationInfoEntity.setCreatedAt(new Date());
        pairRelationInfoEntity.setUpdatedAt(new Date());
        return pairRelationInfoEntity;
    }


    public PairRelationInfo toPairRelationInfo(){
        PairRelationInfo pairRelationInfo = new PairRelationInfo(getName());
        pairRelationInfo.setCreatedAt(getCreatedAt());
        pairRelationInfo.setDesc(getDesc());
        pairRelationInfo.setId(getId());
        pairRelationInfo.setName(getName());
        pairRelationInfo.setParentId(getAppId());
        if(getStatus() == 0) {
            pairRelationInfo.setStatus(Info.InfoStatus.SAVING);
        }
        if(getStatus() == 1) {
            pairRelationInfo.setStatus(Info.InfoStatus.COMPLETE);
        }
        pairRelationInfo.setUpdatedAt(getUpdatedAt());
        return pairRelationInfo;
    }
}