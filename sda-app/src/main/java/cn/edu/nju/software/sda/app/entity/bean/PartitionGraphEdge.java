package cn.edu.nju.software.sda.app.entity.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PartitionGraphEdge {
    private String sourceCommunityId;
    private String targetCommunityId;
    private List<PartitionNodeEdge> edges;
}
