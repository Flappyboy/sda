package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.Git;

import java.util.List;

public interface GitService {

    List<Git> queryGitByAppId(String appId);

    List<Git> queryGit(Git git);
}
