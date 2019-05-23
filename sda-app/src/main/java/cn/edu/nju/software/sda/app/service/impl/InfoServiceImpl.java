package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.dao.InfoManager;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSetMap;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.info.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {

    @Override
    public List<InfoCollectionPlugin> allInfoCollectionPlugins() {
        return InfoCollectionManager.get();
    }

    @Override
    public List<InfoDao> allInfoDao() {
        return InfoDaoManager.allInfoDaos();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void collectInfo(String appId, InfoCollectionPlugin plugin, InputData inputData) {
        File workspace = WorkspaceUtil.workspace("infocollection");
        List<Info> infoList = plugin.processData(inputData, workspace);
        FileUtil.delete(workspace);
        InfoManager.save(appId, infoList);
    }

    @Override
    public InfoCollectionPlugin getInfoCollectionPluginByName(String name) {
        return InfoCollectionManager.get(name);
    }

    @Override
    public InfoSetMap queryInfoByAppId(String appId) {
        List<Info> infoList = InfoManager.querySimpleInfoByApp(appId);
        InfoSetMap infoSetMap = new InfoSetMap();
        for (Info info :
                infoList) {
            infoSetMap.addInfo(info);
        }
        return infoSetMap;
    }
}
