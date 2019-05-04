package cn.edu.nju.software.git;

import cn.edu.nju.software.git.entity.GitCommitFileEdge;
import cn.edu.nju.software.git.entity.GitCommitInfo;
import cn.edu.nju.software.git.entity.GitCommitRetn;

import java.io.IOException;
import java.util.*;

/**
 * git数据处理工具
 */
public class GitDataUtil {

    public static Map<String, GitCommitFileEdge> getCommitFileGraph(GitCommitRetn gitCommitRetn) {
        Map<String, GitCommitFileEdge> gitCommitFileEdgeMap = new HashMap<>();
        List<GitCommitInfo> gitCommitInfos = gitCommitRetn.getGitCommitInfos();
        for (GitCommitInfo gitCommitInfo : gitCommitInfos) {
            Set<String> files = gitCommitInfo.getFiles();
            for (String file1 : files) {
                if (file1.endsWith(".java"))
                    for (String file2 : files) {
                        if (file2.endsWith(".java"))
                            if (file1 != file2) {
                                String class1 = toClassName(file1);
                                String class2 = toClassName(file2);
                                String key = class1 + "-!-" + class2;
                                GitCommitFileEdge oldGitCommitFileEdge = gitCommitFileEdgeMap.get(key);
                                if (oldGitCommitFileEdge == null) {
                                    GitCommitFileEdge gitCommitFileEdge = new GitCommitFileEdge();
                                    gitCommitFileEdge.setCount(1);
                                    gitCommitFileEdge.setSourceName(class1);
                                    gitCommitFileEdge.setTargetName(class2);
                                    gitCommitFileEdgeMap.put(key, gitCommitFileEdge);

                                } else {
                                    int count = oldGitCommitFileEdge.getCount() + 1;
                                    oldGitCommitFileEdge.setCount(count);
                                    gitCommitFileEdgeMap.put(key, oldGitCommitFileEdge);
                                }
                            }
                    }
            }
        }
        return gitCommitFileEdgeMap;
    }

    //文件名转类名
    private static String toClassName(String fileName) {
        String className = "";
        if (fileName.endsWith(".java")) {
            int index = fileName.lastIndexOf(".");
            className = fileName.replace("/", ".").substring(14, index);
        }
        return className;
    }

    public static void main(String[] args) throws Exception {
        Map<String, GitCommitFileEdge> map = getCommitFileGraph(GitUtil.getLocalCommit("/Users/yaya/Documents/mycode/intelliJIdea/journey"));
        System.out.println(map.size());
        for (Map.Entry<String, GitCommitFileEdge> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

    }

}
