package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.hub;

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

