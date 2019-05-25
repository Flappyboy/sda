package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;

import java.util.List;

public interface AppService {
    AppEntity saveApp(AppEntity app);

    AppEntity updateApp(AppEntity app);

    void deleteApp(String appId);

    void deleteApps(List<String> appIds);

    AppEntity queryAppById(String appId);

    List<AppEntity> queryAppListPaged(Integer page, Integer pageSize, AppEntity app);

    PageQueryDto<AppEntity> queryAppListByLikePaged(PageQueryDto<AppEntity> dto, AppEntity app);

    int countOfApp(AppEntity app);

    App getAppWithInfo(String appId, List<String> infoIdList);

    App getAppWithPartition(String partitionId);
}
