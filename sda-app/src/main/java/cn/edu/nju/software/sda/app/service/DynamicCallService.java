package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.DynamicAnalysisInfo;
import cn.edu.nju.software.sda.app.entity.DynamicCallInfo;

import java.util.HashMap;
import java.util.List;

public interface DynamicCallService {
    public void saveDCallInfo(DynamicCallInfo dynamicCallInfo);

    public void statisticsCallInfo(DynamicAnalysisInfo dynamicAnalysisInfo);

    public void updateDCallInfo(DynamicCallInfo dynamicCallInfo);

    public void deleteDCallInfo(String dynamicCallInfoId);

    DynamicCallInfo queryCallById(String id);

    public DynamicCallInfo queryDCallInfo(String dynamicCallInfoId);

    public List<DynamicCallInfo> queryDCallInfo(Integer page, Integer pageSize);
    public List<HashMap<String,String>> findEdgeByAppId(String dynamicAnalysisInfoId, int page, int pageSize, int type);
    public int countOfDynamicCall(String dynamicAnalysisInfoId,int type);

    List<DynamicCallInfo> findByDynamicAndType(String dynamicAnalysisInfoId, int type);
}
