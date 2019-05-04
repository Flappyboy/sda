package cn.edu.nju.software.sda.app.service.git;

import cn.edu.nju.software.git.entity.GitCommitRetn;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface GitService {
    public GitCommitRetn getLocalCommit(String path) throws IOException, GitAPIException;
    public GitCommitRetn getRepositoryCommits(String userName,String repository);
}
