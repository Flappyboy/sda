package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.FileInfo;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import org.apache.commons.io.FileUtils;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class FileInfoDao implements InfoDao<FileInfo> {
    public static final String workName = "FileInfo";

    @Override
    public FileInfo saveProfile(FileInfo info) {
        File folder = WorkspaceUtil.workspace(workName);
        String appId = info.getParentId();
        String infoName = info.getName();
        String infoId = Sid.nextShort();
        info.setId(infoId);

        String path = folder.getPath()+"/"+appId+"/"+infoName+"/"+infoId;
        File fileFolder = new File(path);
        if(fileFolder.exists()){
            FileUtil.delete(fileFolder);
        }
        fileFolder.mkdirs();
        info.setCreatedAt(new Date(fileFolder.lastModified()));
        info.setUpdatedAt(new Date(fileFolder.lastModified()));

        return info;
    }

    @Override
    public FileInfo saveDetail(FileInfo info) {
        File folder = WorkspaceUtil.workspace(workName);
        String appId = info.getParentId();
        String infoName = info.getName();
        String path = folder.getPath()+"/"+appId+"/"+infoName+"/"+info.getId();
        File fileFolder = new File(path);
        try {
            FileUtils.copyFileToDirectory(info.getFile(), fileFolder);
        } catch (IOException e) {
            throw new RuntimeException("Save failed");
        }
        return info;
    }

    @Override
    public FileInfo updateProfileInfoById(FileInfo info) {
        return null;
    }

    @Override
    public FileInfo deleteById(String infoId) {
        File workFolder = WorkspaceUtil.workspace(workName);

        File[] appFolders = workFolder.listFiles();
        for (File appFolder :
                appFolders) {
            if (appFolder.isDirectory()){
                File[] nameFolders = appFolder.listFiles();
                for (File nameFolder :
                        nameFolders) {
                    if (nameFolder.isDirectory()){
                        File[] idFolders = nameFolder.listFiles();
                        for (File idFolder:
                                idFolders) {
                            String id = idFolder.getName();
                            if(id.equals(infoId)){
                                FileUtil.delete(idFolder);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public FileInfo queryProfileInfoById(String infoId) {
        return null;
    }

    @Override
    public List<FileInfo> queryProfileInfoByAppId(String appId) {
        return null;
    }

    @Override
    public List<FileInfo> queryProfileInfoByAppIdAndInfoName(String appId, String infoName) {
        File workFolder = WorkspaceUtil.workspace(workName);

        String path = workFolder.getPath()+"/"+appId+"/"+infoName;
        File fileFolder = new File(path);
        if(!fileFolder.exists() || !fileFolder.isDirectory()) {
            return null;
        }
        List<FileInfo> list = new ArrayList<>();
        File[] folders = fileFolder.listFiles();
        for (File folder :
                folders) {
            if (folder.isDirectory()){
                FileInfo info = new FileInfo(infoName);
                info.setId(folder.getName());

                File[] files =  folder.listFiles();
                if(files.length>0 && files[0].canRead()){
                    info.setStatus(Info.InfoStatus.COMPLETE);
                }else{
                    info.setStatus(Info.InfoStatus.SAVING);
                }
                info.setCreatedAt(new Date(folder.lastModified()));
                info.setUpdatedAt(new Date(folder.lastModified()));
                list.add(info);
            }
        }
        list.sort(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                Date date = o1.getCreatedAt();
                Date date2 = o2.getCreatedAt();
                return date2.compareTo(date);
            }
        });
        return list;
    }

    @Override
    public FileInfo queryDetailInfoById(String infoId) {
        File workFolder = WorkspaceUtil.workspace(workName);

        FileInfo fileInfo;
        File[] appFolders = workFolder.listFiles();
        for (File appFolder :
                appFolders) {
            if (appFolder.isDirectory()){
                File[] nameFolders = appFolder.listFiles();
                for (File nameFolder :
                        nameFolders) {
                    if (nameFolder.isDirectory()){
                        File[] idFolders = nameFolder.listFiles();
                        for (File idFolder:
                                idFolders) {
                            String id = idFolder.getName();
                            if(id.equals(infoId)){
                                fileInfo = new FileInfo(nameFolder.getName());
                                fileInfo.setId(infoId);
                                File[] fs = idFolder.listFiles();
                                if(fs.length>0){
                                    fileInfo.setStatus(Info.InfoStatus.COMPLETE);
                                    fileInfo.setParentId(appFolder.getName());
                                    File file = fs[0];
                                    fileInfo.setFile(file);
                                    return fileInfo;
                                }else{
                                    fileInfo.setStatus(Info.InfoStatus.SAVING);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
