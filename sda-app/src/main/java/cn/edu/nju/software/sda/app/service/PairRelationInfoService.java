package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PairRelationInfoEntity;

import java.util.List;

public interface PairRelationInfoService {
    PairRelationInfoEntity save(PairRelationInfoEntity pairRelationInfoEntity);

    void updateDAnalysisInfo(PairRelationInfoEntity dAnalysisInfo);

    void deleteDAnalysisInfo(String dAnalysisInfoId);

    PairRelationInfoEntity queryDAnalysisInfoById(String dAnalysisInfoId);

    List<PairRelationInfoEntity> queryDAnalysisInfoListPaged(Integer page, Integer pageSize, String appId, String desc);

    int countOfDAnalysisInfo(String appId,String desc);

    //新增接口
    List<PairRelationInfoEntity> queryPairRelationInfoList(String appId, String name);

}
