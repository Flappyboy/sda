package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.PartitionDetailEntity;
import cn.edu.nju.software.sda.app.entity.PartitionNodeEntity;

import java.util.HashMap;
import java.util.List;

public interface PartitionDetailService {
    PartitionDetailEntity savePartitionDetail(PartitionDetailEntity partitionDetailEntity);

    void updatePartitionDetail(PartitionDetailEntity partitionDetailEntity);

    void deletePartitionDetail(String partitionDetailId);

    PartitionDetailEntity queryPartitionDetailById(String partitionDetailId);

    List<HashMap<String, String>> queryPartitionDetailListPaged(String partitionResultId, int type, Integer page, Integer pageSize);

    int countOfPartitionDetail(String partitionResultId, int type);

    List<Object> queryPartitionDetailByResultId(String partitionResultId);

    List<Object> queryPartitionDetailByResultIdPaged(String partitionResultId, Integer page, Integer pageSize);
    int countOfPartitionDetailByResultId(String partitionResultId);

    PartitionGraphOperateDto moveNodeToPartition(String nodeId, PartitionNodeEntity oldPartitionNodeEntity, PartitionNodeEntity targetPartitionNodeEntity);

    PartitionGraphOperateDto removeNodeFromPartition(String nodeId, PartitionNodeEntity oldPartitionNodeEntity);

    PartitionGraphOperateDto addNodeToPartition(String nodeId, PartitionNodeEntity targetPartitionNodeEntity);
}
