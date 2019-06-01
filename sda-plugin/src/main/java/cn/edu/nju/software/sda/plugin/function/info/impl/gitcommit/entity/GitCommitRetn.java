package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class GitCommitRetn {
    private List<GitCommitInfo> gitCommitInfos;
    private List<FileInfo> fileInfos;
}
