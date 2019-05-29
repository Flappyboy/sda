package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.info.GroupRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Table(name = "group_relation")
public class GroupRelationEntity {

    @Id
    private String id;

    private String infoId;

    private String nodes;

    private Double value;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private List<NodeEntity> nodeObjList;

    public static GroupRelationEntity createNewEntity(String infoId, GroupRelation groupRelation){
        GroupRelationEntity groupRelationEntity = new GroupRelationEntity();
        groupRelationEntity.setId(groupRelation.getId());
        groupRelationEntity.setInfoId(infoId);
        groupRelationEntity.setValue(groupRelation.getValue());
        groupRelationEntity.setFlag(1);
        groupRelationEntity.setCreatedAt(new Date());
        groupRelationEntity.setUpdatedAt(new Date());
        NodeSet nodeSet = groupRelation.getNodeSet();
        StringBuilder nodesBuilder = new StringBuilder();
        for(Node node :nodeSet){
            nodesBuilder.append(node.getId());
            nodesBuilder.append(";");
        }
        groupRelationEntity.setNodes(nodesBuilder.toString());
        return groupRelationEntity;
    }
    public static List<GroupRelationEntity> createNewEntities(String infoId, List<GroupRelation> groupRelations){
        List<GroupRelationEntity> entities = new ArrayList<>();
        for (GroupRelation relation :
                groupRelations) {
            entities.add(createNewEntity(infoId, relation));
        }
        return entities;
    }

    public GroupRelation toGroupRelation(Map<String, Node> idNodeMap){
        GroupRelation groupRelation = new GroupRelation(getId(),getValue());
        String nodes = getNodes();
        String[] nodeIds = nodes.split(";");
        for(int i = 0; i <nodeIds.length; i++) {
            groupRelation.addNode(idNodeMap.get(nodeIds[i]));
        }
        return groupRelation;
    }

}
