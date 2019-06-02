package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity;

import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GitCommitFileEdge {
    private String sourceName;
    private String targetName;
    private double count;    //int -> double

    public GitCommitFileEdge(PairRelation pairRelation){
        this.sourceName =pairRelation.getSourceNode().getName();
        this.targetName =pairRelation.getTargetNode().getName();
        this.count =pairRelation.getValue();
    }

    public GitCommitFileEdge(String sourceName, String targetName, double count){
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.count = count;

    }

}

