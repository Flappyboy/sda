package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.service.FunctionService;

import java.util.List;

public interface InfoService {

    List<InfoDao> allInfoDao();

    InfoSet queryInfoByAppId(String appId);

    InfoSet queryInfoByAppIdAndName(String appId, String name);

    InfoSet queryInfoNameByAppId(String appId);
}
