package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PairRelationEntity;
import cn.edu.nju.software.sda.app.entity.PairRelationInfoEntity;

import java.util.List;

public interface PairRelationService {
    void save(PairRelationEntity pairRelationEntity);

    void statisticsCallInfo(PairRelationInfoEntity pairRelationInfoEntity);

    void update(PairRelationEntity pairRelationEntity);

    void delete(String dynamicCallInfoId);

    PairRelationEntity queryCallById(String id);

    List<PairRelationEntity> findByDynamicAndType(String dynamicAnalysisInfoId, int type);

    List<PairRelationEntity> pairRelationsForNode(String nodeId, List<String> pairRelationInfoId);
}