package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.App;
import cn.edu.nju.software.sda.app.entity.adapter.AppAdapter;

import java.util.List;

public interface AppService {
    App saveApp(App app);

    void updateApp(App app);

    void deleteApp(String appId);

    App queryAppById(String appId);

    List<App> queryUserListPaged(Integer page, Integer pageSize,String appName,String desc);

    int countOfApp(String appName,String desc);


    AppAdapter getAppWithInfo(String appId, List<String> infoIdList);

    AppAdapter getAppWithPartition(String partitionId);
}
