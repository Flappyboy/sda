package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.core.domain.App;

import java.util.List;

public interface AppService {
    AppEntity saveApp(AppEntity app);

    void updateApp(AppEntity app);

    void deleteApp(String appId);

    AppEntity queryAppById(String appId);

    List<AppEntity> queryUserListPaged(Integer page, Integer pageSize, AppEntity app);

    int countOfApp(AppEntity app);

    App getAppWithInfo(String appId, List<String> infoIdList);

    App getAppWithPartition(String partitionId);
}
