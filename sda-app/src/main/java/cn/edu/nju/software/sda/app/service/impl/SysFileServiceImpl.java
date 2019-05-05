package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.service.SdaService;
import cn.edu.nju.software.sda.app.service.SysFileService;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SysFileServiceImpl implements SysFileService {
    @Value("${filepath}")
    private String path;

    @Override
    public File getWorkspace(SdaService service) {
        String dirName = path+service.getName()+"/"+Sid.next();
        File file = new File(dirName);
        file.mkdirs();
        return file;
    }
}
