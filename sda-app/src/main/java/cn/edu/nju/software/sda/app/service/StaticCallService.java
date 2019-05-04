package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.App;
import cn.edu.nju.software.sda.app.entity.StaticCallInfo;

import java.util.HashMap;
import java.util.List;

public interface StaticCallService {

    StaticCallInfo queryCallById(String id);

    public void saveStaticAnalysis(App app, Integer flag) throws Exception;
    public List<HashMap<String,String>> findEdgeByAppId(String appid,int page,int pageSize,int type);
    public int countOfStaticAnalysis(String appid,int type);

    List<StaticCallInfo> findByAppIdAndType(String appId, int type);
}
