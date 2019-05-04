package cn.edu.nju.software.sda.app.entity.common.github;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommitEntity {
    private Author author;
    private Author committer;
    private String message;
    private Tree tree;
    private String url;
    private int comment_count;
    private Verification verification;
}
