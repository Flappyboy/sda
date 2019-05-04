package cn.edu.nju.software.git.entity;

import cn.edu.nju.software.git.entity.FileInfo;
import cn.edu.nju.software.git.entity.GitCommitInfo;
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
