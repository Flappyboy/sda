package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.info.GroupRelationInfo;
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
@Table(name = "group_relation_info")
public class GroupRelationInfoEntity implements Serializable {
    @Id
    String id;

    String appId;

    String type;

    String name;

    @Column(name = "`desc`")
    String desc;

    Integer status;

    Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private String appName;

    @Transient
    List<GroupRelationEntity> groupRelationEntityList;

    public static GroupRelationInfoEntity createNewEntity(GroupRelationInfo groupRelationInfo){
        GroupRelationInfoEntity groupRelationInfoEntity = new GroupRelationInfoEntity();
        groupRelationInfoEntity.setId(groupRelationInfo.getId());
        groupRelationInfoEntity.setAppId(groupRelationInfo.getParentId());
        groupRelationInfoEntity.setDesc(groupRelationInfo.getDesc());
        groupRelationInfoEntity.setFlag(1);
        groupRelationInfoEntity.setName(groupRelationInfo.getName());
        groupRelationInfoEntity.setStatus(Info.InfoStatus.COMPLETE.equals(groupRelationInfo.getStatus())? 1 : 0);
        groupRelationInfoEntity.setCreatedAt(new Date());
        groupRelationInfoEntity.setUpdatedAt(new Date());
        return groupRelationInfoEntity;
    }

    public GroupRelationInfo toGroupRelationInfo(){
        GroupRelationInfo groupRelationInfo = new GroupRelationInfo(getDesc());
        groupRelationInfo.setCreatedAt(getCreatedAt());
        groupRelationInfo.setDesc(getDesc());
        groupRelationInfo.setId(getId());
        groupRelationInfo.setName(getName());
        groupRelationInfo.setParentId(getAppId());
        if(getStatus() == 0) {
            groupRelationInfo.setStatus(Info.InfoStatus.SAVING);
        }
        if(getStatus() == 1) {
            groupRelationInfo.setStatus(Info.InfoStatus.COMPLETE);
        }
        groupRelationInfo.setUpdatedAt(getUpdatedAt());
        return groupRelationInfo;
    }
}
