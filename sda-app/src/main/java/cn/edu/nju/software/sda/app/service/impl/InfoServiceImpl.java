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
import cn.edu.nju.software.sda.core.exception.WorkFailedException;
import cn.edu.nju.software.sda.core.service.FunctionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {

    @Override
    public List<InfoDao> allInfoDao() {
        return InfoDaoManager.allInfoDaos();
    }

    @Override
    public InfoSet queryInfoByAppId(String appId) {
        List<Info> infoList = InfoManager.querySimpleInfoByApp(appId);
        InfoSet infoSet = new InfoSet(infoList);
        return infoSet;
    }

    @Override
    public InfoSet queryInfoByAppIdAndName(String appId, String name) {
        if(StringUtils.isBlank(name)){
            return queryInfoByAppId(appId);
        }
        List<Info> infoList = InfoManager.queryInfoByAppIdAndName(appId, name);
        InfoSet infoSet = new InfoSet(infoList);
        return infoSet;
    }

    @Override
    public InfoSet queryInfoNameByAppId(String appId) {
        return null;
    }
}
