package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PartitionInfoEntity;
import cn.edu.nju.software.sda.app.entity.PartitionNodeEntity;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;

import java.util.List;

public interface PartitionResultService {
    public PartitionNodeEntity savePartitionResult(PartitionNodeEntity partitionNodeEntity);

    public void updatePartitionResult(PartitionNodeEntity partitionNodeEntity);

    public void deletePartitionResult(String partitionResultId);

    PartitionNodeEntity queryPartitionResultById(String partitionResultId);

    public List<PartitionNodeEntity> queryPartitionResultListPaged(String dynamicInfoId, String algorithmsId, int type, Integer page, Integer pageSize);

    GraphDto queryPartitionResultForDto(String partitionId);

    List<PartitionNodeEntity> queryPartitionResult(String partitionId);
    List<String> findPartitionResultIds(String partitionId);

    PartitionNodeEntity queryPartitionResult(String partitionId, String partitionResultName);

    public void partition(PartitionInfoEntity partitionInfoEntity) throws Exception;

    public int countOfPartitionResult(String dynamicInfoId,String algorithmsId,int type);

    int countOfPartitionResult(String partitionId);
}
