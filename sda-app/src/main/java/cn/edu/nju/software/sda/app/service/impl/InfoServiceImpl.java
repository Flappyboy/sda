package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.dao.InfoManager;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.exception.WorkFailedException;
import cn.edu.nju.software.sda.plugin.function.info.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {

    @Override
    public List<InfoCollection> allInfoCollectionPlugins() {
        return InfoCollectionManager.get();
    }

    @Override
    public List<InfoDao> allInfoDao() {
        return InfoDaoManager.allInfoDaos();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void collectInfo(String appId, InfoCollection plugin, InputData inputData) {
        File workspace = WorkspaceUtil.workspace("infocollection");
        Work work = new Work();
        work.setWorkspace(workspace);
        InfoSet infoSet = null;
        try {
            infoSet = plugin.work(inputData, work);
        } catch (WorkFailedException e) {
            e.printStackTrace();
        }
        FileUtil.delete(workspace);
        InfoManager.save(appId, infoSet.getInfoList());
    }

    @Override
    public InfoCollection getInfoCollectionPluginByName(String name) {
        return InfoCollectionManager.get(name);
    }

    @Override
    public InfoSet queryInfoByAppId(String appId) {
        List<Info> infoList = InfoManager.querySimpleInfoByApp(appId);
        InfoSet infoSet = new InfoSet();
        for (Info info :
                infoList) {
            infoSet.addInfo(info);
        }
        return infoSet;
    }
}
