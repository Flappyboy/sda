package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.CallInfo;

import java.util.List;

public interface CallService {

    /**
     * 找到当前动态数据和静态数据下，这个节点的所有调用信息
     * @param nodeId
     * @param dynamicAnalysisInfoId
     * @return
     */
    List<CallInfo> findCallInfo(String nodeId, String dynamicAnalysisInfoId);
}
