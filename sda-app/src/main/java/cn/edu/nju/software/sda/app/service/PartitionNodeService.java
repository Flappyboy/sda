package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PartitionNodeEntity;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;

import java.util.List;

public interface PartitionNodeService {
    PartitionNodeEntity savePartitionResult(PartitionNodeEntity partitionNodeEntity);

    void updatePartitionResult(PartitionNodeEntity partitionNodeEntity);

    void deletePartitionResult(String partitionResultId);

    PartitionNodeEntity queryPartitionResultById(String partitionResultId);

    List<PartitionNodeEntity> queryPartitionResultListPaged(String dynamicInfoId, String algorithmsId, int type, Integer page, Integer pageSize);

    GraphDto queryPartitionResultForDto(String partitionId);

    List<PartitionNodeEntity> queryPartitionResult(String partitionId);
    List<String> findPartitionResultIds(String partitionId);

    PartitionNodeEntity queryPartitionResult(String partitionId, String partitionResultName);

    int countOfPartitionResult(String dynamicInfoId,String algorithmsId,int type);

    int countOfPartitionResult(String partitionId);
}
