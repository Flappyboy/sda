package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GitCommitFileEdge {
    private String sourceName;
    private String targetName;
    private int count;    //int -> double

    public GitCommitFileEdge(){

    }

    public GitCommitFileEdge(String sourceName, String targetName, int count){
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.count = count;

    }
}

