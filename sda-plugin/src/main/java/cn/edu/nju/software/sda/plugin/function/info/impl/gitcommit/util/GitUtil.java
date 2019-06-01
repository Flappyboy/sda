package cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.util;

import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.hub.CommitDetail;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.FileInfo;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.GitCommitInfo;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.GitCommitRetn;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.hub.CommitFile;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.hub.CommitStats;
import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.util.HttpClientUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * git项目信息获取工具
 */

public class GitUtil {


    /**
     * 获取提交信息
     * @param flag  1-获取本地，2-获取github上的
     * @param path  本地路径/github地址
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    public static GitCommitRetn getGitCommitInfo(Integer flag, String path) throws IOException, GitAPIException {
        GitCommitRetn gitCommitRetn = new GitCommitRetn();
//        https://github.com/WCXwcx/PetStore.git
        if (flag == null || flag == 1) {
//            gitCommitRetn = gitService.getLocalCommit("/Users/yaya/Documents/mycode/intelliJIdea/journey/");
            gitCommitRetn =getLocalCommit(path);
        }
        if (flag == 2) {
            String[] paths = path.substring(19).split("/");
            String username = paths[0];
            int index = paths[1].lastIndexOf(".");
            String repository = paths[1].substring(0, index);
            System.out.println(username);
            System.out.println(repository);
            gitCommitRetn = getRepositoryCommits(username, repository);
        }

        return gitCommitRetn;
    }

    /**
     * 获取本地提交信息
     * @param path
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    public static GitCommitRetn getLocalCommit(String path) throws IOException, GitAPIException {
        List<GitCommitInfo> gitCommitInfos = new ArrayList<>();
        List<FileInfo> fileInfos = new ArrayList<>();
        GitCommitRetn gitCommitRetn = new GitCommitRetn();
        HashMap<String, FileInfo> fileMap = new HashMap<>();

        if (!path.endsWith("/"))
            path = path + "/";
        String gitPath = path + ".git";
        // 打开一个存在的仓库
        Repository existingRepo = new FileRepositoryBuilder()
                .setGitDir(new File(gitPath))
                .build();
        try (Git git = new Git(existingRepo)) {
            Iterable<RevCommit> commits = git.log().all().call();
            int count = 0;
            for (RevCommit commit : commits) {
                String commitName = commit.getName();
                RevCommit[] parentsCommit = commit.getParents();
                int parentsCount = commit.getParentCount();
                Set<String> files = new HashSet<>();
                List<String> parentNames = new ArrayList<>();
                GitCommitInfo gitCommitInfo = new GitCommitInfo();
                gitCommitInfo.setCommitName(commitName);
                for (int i = 0; i < parentsCount; i++) {
                    Set<String> filesTmp = listDiff(existingRepo, git,
                            parentsCommit[i].getName(),
                            commitName);
                    files.addAll(filesTmp);
                    parentNames.add(parentsCommit[i].getName());
                }
                gitCommitInfo.setCommitParentName(parentNames);
                gitCommitInfo.setFiles(files);
                gitCommitInfos.add(gitCommitInfo);
                count++;
            }
            fileInfos = getFiles(gitCommitInfos);
//            System.out.println(count);
        }
        gitCommitRetn.setFileInfos(fileInfos);
        gitCommitRetn.setGitCommitInfos(gitCommitInfos);
        return gitCommitRetn;
    }

    /**
     * 获取github的提交信息
     * @param userName
     * @param repository
     * @return
     */
    public static GitCommitRetn getRepositoryCommits(String userName, String repository) {
        List<GitCommitInfo> gitCommitInfos = new ArrayList<>();
        List<FileInfo> fileInfos = new ArrayList<>();
        GitCommitRetn gitCommitRetn = new GitCommitRetn();

        List<CommitDetail> commitLists1 = new ArrayList<>();

        String listStr = HttpClientUtil.doGet("https://api.github.com/repos/" + userName + "/" + repository + "/commits");
//        System.out.println("1");
//        System.out.println(listStr);
        if (listStr == null)
            return null;
        JSONArray listArray = new JSONArray(listStr);
        for (int i = 0; i < listArray.length(); i++) {
            GitCommitInfo gitCommitInfo = new GitCommitInfo();

            JSONObject listJsonObject = listArray.getJSONObject(i);

            String url = "https://api.github.com/repos/" + userName + "/" + repository +"/commits/" + listJsonObject.getString("sha");
            String detailStr = HttpClientUtil.doGet(url);
//            System.out.println("2");
//            System.out.println(detailStr);
            if (detailStr == null)
                return null;
            JSONObject detailJsonObject = new JSONObject(detailStr);

            CommitDetail commitDetail = new CommitDetail();
            commitDetail.setSha(detailJsonObject.getString("sha"));
            commitDetail.setNode_id(detailJsonObject.getString("node_id"));

            List<String> parents_sha = new ArrayList<>();
            JSONArray parentsJson = detailJsonObject.getJSONArray("parents");
            for (int j = 0; j < parentsJson.length(); j++) {
                parents_sha.add(parentsJson.getJSONObject(j).getString("sha"));
            }
            commitDetail.setParents_sha(parents_sha);

            JSONObject statsJson = detailJsonObject.getJSONObject("stats");
            CommitStats commitStats = new CommitStats();
            commitStats.setAdditions(statsJson.getInt("additions"));
            commitStats.setDeletions(statsJson.getInt("deletions"));
            commitStats.setTotal(statsJson.getInt("total"));
            commitDetail.setStats(commitStats);

            List<CommitFile> files = new ArrayList<>();
            Set<String> fileNames = new HashSet<>();
            JSONArray filesJson = detailJsonObject.getJSONArray("files");
            for (int j = 0; j < filesJson.length(); j++) {
                CommitFile file = new CommitFile();
                file.setFilename(filesJson.getJSONObject(j).getString("filename"));
                file.setAdditions(filesJson.getJSONObject(j).getInt("additions"));
                file.setBlob_url(filesJson.getJSONObject(j).getString("blob_url"));
                file.setChanges(filesJson.getJSONObject(j).getInt("changes"));
                file.setContents_url(filesJson.getJSONObject(j).getString("contents_url"));
                file.setDeletions(filesJson.getJSONObject(j).getInt("deletions"));
//                file.setPatch(filesJson.getJSONObject(j).getString("patch"));
                file.setRaw_url(filesJson.getJSONObject(j).getString("raw_url"));
                file.setStatus(filesJson.getJSONObject(j).getString("status"));
                file.setSha(filesJson.getJSONObject(j).getString("sha"));
                files.add(file);
                fileNames.add(filesJson.getJSONObject(j).getString("filename"));
            }
            commitDetail.setFiles(files);
            commitLists1.add(commitDetail);

            gitCommitInfo.setCommitName(listJsonObject.getString("sha"));
            gitCommitInfo.setCommitParentName(parents_sha);
            gitCommitInfo.setFiles(fileNames);

            gitCommitInfos.add(gitCommitInfo);

        }

        fileInfos = getFiles(gitCommitInfos);
        gitCommitRetn.setFileInfos(fileInfos);
        gitCommitRetn.setGitCommitInfos(gitCommitInfos);

        return gitCommitRetn;
    }


    /**
     * 根据提交信息统计文件个数
     * @param gitCommitInfos
     * @return
     */
    private static List<FileInfo> getFiles(List<GitCommitInfo> gitCommitInfos) {
        List<FileInfo> fileInfos = new ArrayList<>();
        HashMap<String, FileInfo> fileMap = new HashMap<>();
        for (GitCommitInfo gitCommitInfo : gitCommitInfos) {
            Set<String> files = gitCommitInfo.getFiles();
            for (String file : files) {
                FileInfo fileInfo = fileMap.get(file);
                if (fileInfo != null) {
                    int newCount = fileInfo.getCount() + 1;
                    fileInfo.setCount(newCount);
                    fileMap.put(file, fileInfo);
                } else {
                    FileInfo newFileInfo = new FileInfo();
                    newFileInfo.setCount(1);
                    newFileInfo.setFileName(file);
                    fileMap.put(file, newFileInfo);
                }
            }

        }
        for (Map.Entry<String, FileInfo> entry : fileMap.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            fileInfos.add(entry.getValue());
        }
        return fileInfos;
    }


    /**
     * 比较两次提交之间修改了的文件
     * @param repository
     * @param git
     * @param oldCommit
     * @param newCommit
     * @return
     * @throws GitAPIException
     * @throws IOException
     */
    private static Set<String> listDiff(Repository repository, Git git, String oldCommit, String newCommit) throws GitAPIException, IOException {
        Set<String> files = new HashSet<>();
        final List<DiffEntry> diffs = git.diff()
                .setOldTree(prepareTreeParser(repository, oldCommit))
                .setNewTree(prepareTreeParser(repository, newCommit))
                .call();

//        System.out.println("Found: " + diffs.size() + " differences");
        for (DiffEntry diff : diffs) {
//            System.out.println("Diff: " + diff.getChangeType() + ": " +
//                    (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
            files.add(diff.getNewPath());
        }
        return files;
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }

    public static void main(String[] args) throws IOException, GitAPIException {
        System.out.println(getLocalCommit("/Users/yaya/Documents/mycode/intelliJIdea/springboot-start/"));
//        System.out.println(getRepositoryCommits("yzgqy","journey"));
//        System.out.println(getGitCommitInfo(1,"/Users/yaya/Documents/mycode/intelliJIdea/journey/")
//        );

    }
}

