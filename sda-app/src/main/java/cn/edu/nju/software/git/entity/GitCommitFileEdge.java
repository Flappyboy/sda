package cn.edu.nju.software.git.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GitCommitFileEdge {
    private String sourceName;
    private String targetName;
    private int count;
}
