package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PartitionInfoEntity;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;

import java.util.HashMap;
import java.util.List;

public interface PartitionService {
    PartitionInfoEntity findPartitionById(String partitionId);
    void addPartition(PartitionInfoEntity partition);
    void delPartition(String partitionInfoId);
    void updatePartition(PartitionInfoEntity partition);
    List<HashMap<String ,Object>> findBycondition(Integer page, Integer pageSize, String algorithmsid, Integer type);
    int count(String algorithmsid,Integer type) ;
//    PartitionGraph getGraph(String partitionInfoId);

    Evaluation evaluate(String partitionId, String evaluationPluginName);
}
