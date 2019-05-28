package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.Git;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;

import cn.edu.nju.software.sda.app.service.GitService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "git相关接口")
@RequestMapping(value = "/api")
public class GitController {
    @Autowired
    private GitService gitService;

    @RequestMapping(value= "/git", method = RequestMethod.GET)
    public JSONResult getGit(Git git, Integer page, Integer pageSize){
        Page pageHelper = PageHelper.startPage(page, pageSize, true);
        List<Git> gitList = gitService.queryGit(git);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",gitList);
        result.put("total",pageHelper.getTotal());
        return JSONResult.ok(result);
    }

//    @RequestMapping(data = "/git", method = RequestMethod.GET)
//    public JSONResult getGitCommitInfo(Integer flag, String path) throws IOException, GitAPIException {
//        GitCommitRetn gitCommitRetn = new GitCommitRetn();
////        https://github.com/WCXwcx/PetStore.git
//        if (flag == null || flag == 1) {
////            gitCommitRetn = gitService.getLocalCommit("/Users/yaya/Documents/mycode/intelliJIdea/journey/");
//            gitCommitRetn = gitService.getLocalCommit(path);
//        }
//        if (flag == 2) {
////            String username=;
////            String repository=;
////            gitCommitRetn = gitService.getRepositoryCommits("yzgqy","journey");
//            String[] paths = path.substring(19).split("/");
//            String username = paths[0];
//            int index = paths[1].lastIndexOf(".");
//            String repository = paths[1].substring(0, index);
//            System.out.println(username);
//            System.out.println(repository);
//            gitCommitRetn = gitService.getRepositoryCommits(username, repository);
//        }
//
//        return JSONResult.ok(gitCommitRetn);
//    }

    public static void main(String[] args) {
        String a = "https://github.com/WCXwcx/PetStore.git";
        String[] a1 = a.substring(19).split("/");
        int index = a1[1].lastIndexOf(".");
        System.out.println(a1[1].substring(0, index));
    }
}
