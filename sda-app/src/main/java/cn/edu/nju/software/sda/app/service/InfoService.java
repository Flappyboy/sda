package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;

import java.util.List;

public interface InfoService {

    List<InfoCollection> allInfoCollectionPlugins();

    List<InfoDao> allInfoDao();

    void collectInfo(String appId, InfoCollection plugin, InputData inputData);

    InfoCollection getInfoCollectionPluginByName(String name);

    InfoSet queryInfoByAppId(String appId);
}
