package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PartitionInfo;
import cn.edu.nju.software.sda.app.entity.bean.PartitionGraph;
import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.evaluation.Evaluation;

import java.util.HashMap;
import java.util.List;

public interface PartitionService {
    PartitionInfo findPartitionById(String partitionId);
    void addPartition(PartitionInfo partition);
    void delPartition(String partitionInfoId);
    void updatePartition(PartitionInfo partition);
    List<HashMap<String ,Object>> findBycondition(Integer page, Integer pageSize, String appName, String desc, String algorithmsid, Integer type);
    int count(String appName,String desc,String algorithmsid,Integer type) ;
    PartitionGraph getGraph(String partitionInfoId);

    Evaluation evaluate(String partitionId, String evaluationPluginName);
}
