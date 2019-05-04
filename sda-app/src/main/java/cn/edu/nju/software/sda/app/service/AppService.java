package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.App;

import java.util.List;

public interface AppService {
    public App saveApp(App app);

    public void updateApp(App app);

    public void deleteApp(String appId);

    public App queryAppById(String appId);

    public List<App> queryUserListPaged(Integer page, Integer pageSize,String appName,String desc);

    public int countOfApp(String appName,String desc);

}
