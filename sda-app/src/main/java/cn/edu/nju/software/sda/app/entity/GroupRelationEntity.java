package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "group_relation")
public class GroupRelationEntity {

    @Id
    private String id;

    private String nodes;

    private Integer value;

    private Integer flag;

    @Transient
    private List<NodeEntity> nodeObjList;
}
