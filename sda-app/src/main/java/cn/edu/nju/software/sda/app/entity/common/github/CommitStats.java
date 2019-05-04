package cn.edu.nju.software.sda.app.entity.common.github;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommitStats {
    private int total;
    private int additions;
    private int deletions;
}
