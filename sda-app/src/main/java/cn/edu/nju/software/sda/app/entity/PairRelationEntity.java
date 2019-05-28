package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.app.dto.InfoDto;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.node.Node;
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
@Table(name = "pair_relation")
public class PairRelationEntity {

    @Id
    private String id;

    private String infoId;

    private String sourceNode;

    private String targetNode;

    private Double value;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private String infoName;

    @Transient
    private NodeEntity sourceNodeObj;

    @Transient
    private NodeEntity targetNodeObj;

    public static PairRelationEntity createNewEntity(String infoId, PairRelation pairRelation){
        PairRelationEntity pairRelationEntity = new PairRelationEntity();
        pairRelationEntity.setId(pairRelation.getId());
        pairRelationEntity.setInfoId(infoId);
        pairRelationEntity.setSourceNode(pairRelation.getSourceNode().getId());
        pairRelationEntity.setTargetNode(pairRelation.getTargetNode().getId());
        pairRelationEntity.setValue(pairRelation.getValue());
        pairRelationEntity.setFlag(1);
        pairRelationEntity.setCreatedAt(new Date());
        pairRelationEntity.setUpdatedAt(new Date());
        return pairRelationEntity;
    }
    public static List<PairRelationEntity> createNewEntities(String infoId, List<PairRelation> pairRelationList){
        List<PairRelationEntity> entities = new ArrayList<>();
        for (PairRelation node :
                pairRelationList) {
            entities.add(createNewEntity(infoId, node));
        }
        return entities;
    }

    public PairRelation toPairRelation(Map<String, Node> idNodeMap){
        Node sourceNode = idNodeMap.get(getSourceNode());
        Node targetNode = idNodeMap.get(getTargetNode());
        PairRelation pairRelation = new PairRelation(getId(),getValue(),sourceNode,targetNode);
        return pairRelation;
    }
}
