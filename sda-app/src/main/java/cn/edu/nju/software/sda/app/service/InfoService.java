package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.plugin.function.PluginFunction;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;

import java.util.List;

public interface InfoService {

    List<InfoDao> allInfoDao();

    void collectInfo(String appId, PluginFunction function, InputData inputData);


    InfoSet queryInfoByAppId(String appId);
}
