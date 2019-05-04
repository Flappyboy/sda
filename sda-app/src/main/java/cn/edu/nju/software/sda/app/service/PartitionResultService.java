package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PartitionInfo;
import cn.edu.nju.software.sda.app.entity.PartitionResult;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;

import java.util.List;

public interface PartitionResultService {
    public PartitionResult savePartitionResult(PartitionResult partitionResult);

    public void updatePartitionResult(PartitionResult partitionResult);

    public void deletePartitionResult(String partitionResultId);

    PartitionResult queryPartitionResultById(String partitionResultId);

    public List<PartitionResult> queryPartitionResultListPaged(String dynamicInfoId,String algorithmsId,int type,Integer page, Integer pageSize);

    GraphDto queryPartitionResultForDto(String partitionId);

    List<PartitionResult> queryPartitionResult(String partitionId);
    List<String> findPartitionResultIds(String partitionId);

    PartitionResult queryPartitionResult(String partitionId, String partitionResultName);

    public void partition(PartitionInfo partitionInfo) throws Exception;

    public int countOfPartitionResult(String dynamicInfoId,String algorithmsId,int type);

    int countOfPartitionResult(String partitionId);
}
