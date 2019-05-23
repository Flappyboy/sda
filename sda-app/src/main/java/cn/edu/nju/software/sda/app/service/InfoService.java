package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.InfoSetMap;
import cn.edu.nju.software.sda.plugin.info.InfoCollectionPlugin;

import java.util.List;

public interface InfoService {

    List<InfoCollectionPlugin> allInfoCollectionPlugins();

    List<InfoDao> allInfoDao();

    void collectInfo(String appId, InfoCollectionPlugin plugin, InputData inputData);

    InfoCollectionPlugin getInfoCollectionPluginByName(String name);

    InfoSetMap queryInfoByAppId(String appId);
}
