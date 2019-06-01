package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit;

import cn.edu.nju.software.sda.core.domain.info.GroupRelation;
import cn.edu.nju.software.sda.core.domain.info.GroupRelationInfo;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.GitCommitFileEdge;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.GitCommitInfo;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.GitCommitRetn;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.util.GetAllFiles;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.util.GitUtil;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GitCommitData {
    NodeSet nodeSet = new NodeSet();
    GroupRelationInfo classGroup = new GroupRelationInfo(GroupRelation.GIT_COMMIT);
    public GitCommitData() {}
    public GitCommitData(String gitPath,List<String> prefixes) {
        //获取gitcommit数据
        GitCommitRetn gitCommitRetn = new GitCommitRetn();
        try {
            gitCommitRetn = GitUtil.getLocalCommit(gitPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取当前文件项目文件数据
        HashSet<String> nowFiles = new HashSet<>();
        nowFiles.addAll(new GetAllFiles().getNowAllFiles(gitPath));

        List<GitCommitInfo> gitCommitInfos = gitCommitRetn.getGitCommitInfos();
        for (GitCommitInfo gitCommitInfo : gitCommitInfos) {
            Set<String> files = gitCommitInfo.getFiles();
            for (String file : files) {
                if (file.endsWith(".java") && !file.endsWith("Test.java")&& nowFiles.contains(file)){
                    
                }
//                    for (String file2 : files) {
//                        if (file2.endsWith(".java") && !file2.endsWith("Test.java") && nowFiles.contains(file2))
//                            if (file1 != file2) {
//                                String class1 = toClassName(file1);
//                                String class2 = toClassName(file2);
//                                String key = class1 + "-!-" + class2;
//                                GitCommitFileEdge oldGitCommitFileEdge = gitCommitFileEdgeMap.get(key);
//                                if (oldGitCommitFileEdge == null) {
//                                    GitCommitFileEdge gitCommitFileEdge = new GitCommitFileEdge();
//                                    gitCommitFileEdge.setCount(1);
//                                    gitCommitFileEdge.setSourceName(class1);
//                                    gitCommitFileEdge.setTargetName(class2);
//                                    gitCommitFileEdgeMap.put(key, gitCommitFileEdge);
//
//                                } else {
//                                    int count = oldGitCommitFileEdge.getCount() + 1;
//                                    oldGitCommitFileEdge.setCount(count);
//                                    gitCommitFileEdgeMap.put(key, oldGitCommitFileEdge);
//                                }
//                            }
//                    }
            }
        }

    }

    //文件名转类名
    private static String toClassName(String fileName,List<String> prefixes) {
        String className = "";
        if (fileName.endsWith(".java")) {
            int len = 0;
            for(String prefix:prefixes){
                if(fileName.startsWith(prefix)){
                    int prefixLen = prefix.length();
                    if(prefixLen>len)
                        className = fileName.substring(prefixLen);
                }
            }
            int index = className.lastIndexOf(".");
            className = className.replace("/", ".").substring(0, index);
        }
        return className;
    }

    public static void main(String[] args) {
        String s = "scr/1/2/com/test/A.java";
        List<String> prefixes = new ArrayList<>();
        prefixes.add("scr/1/");
        prefixes.add("scr/1/1/");
        prefixes.add("scr/1/3/");
        prefixes.add("scr/1/2/");
        System.out.println(toClassName(s,prefixes));

    }
}
