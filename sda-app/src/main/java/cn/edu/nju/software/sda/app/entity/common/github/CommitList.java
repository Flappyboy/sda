package cn.edu.nju.software.sda.app.entity.common.github;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Setter
@Getter
@ToString
public class CommitList {
    private String sha;
    private String node_id;
    private CommitEntity commit;
    private String url;
    private String html_url;
    private String comments_url;
    private Author author;
    private Author committer;
    private List<CommitParent> parents;
}
