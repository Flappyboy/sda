package cn.edu.nju.software.sda.app.entity.common.github;

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
