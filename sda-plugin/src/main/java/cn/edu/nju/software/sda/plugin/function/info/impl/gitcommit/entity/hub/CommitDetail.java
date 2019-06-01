package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Setter
@Getter
@ToString
public class CommitDetail {
    private String sha;
    private String node_id;
    private List<String> parents_sha;
    private CommitStats stats;
    private List<CommitFile> files;
}