package cn.edu.nju.software.git.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
public class GitCommitInfo {
    private String commitName;
    private List<String> commitParentName;
    private Set<String> files;
}
