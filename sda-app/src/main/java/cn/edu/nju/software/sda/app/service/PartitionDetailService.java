package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.NodeEntity;
import cn.edu.nju.software.sda.app.entity.PartitionDetailEntity;
import cn.edu.nju.software.sda.app.entity.PartitionNodeEntity;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;

import java.util.HashMap;
import java.util.List;

public interface PartitionDetailService {
    PartitionDetailEntity savePartitionDetail(PartitionDetailEntity partitionDetailEntity);

    void updatePartitionDetail(PartitionDetailEntity partitionDetailEntity);

    void deletePartitionDetail(String partitionDetailId);

    PartitionDetailEntity queryPartitionDetailById(String partitionDetailId);

    List<PartitionDetailEntity> queryPartitionDetailListByPartitionNodeId(String partitionNodeId);

    List<HashMap<String, String>> queryPartitionDetailListPaged(String partitionResultId, int type, Integer page, Integer pageSize);

    int countOfPartitionDetail(String partitionResultId, int type);

    List<Object> queryPartitionDetailByResultId(String partitionResultId);

    PageQueryDto<NodeEntity> queryPartitionDetailByResultIdPaged(PageQueryDto<NodeEntity> dto, String partitionResultId);

    int countOfPartitionDetailByResultId(String partitionResultId);

    PartitionGraphOperateDto moveNodeToPartition(String nodeId, PartitionNodeEntity oldPartitionNodeEntity, PartitionNodeEntity targetPartitionNodeEntity);

    PartitionGraphOperateDto removeNodeFromPartition(String nodeId, PartitionNodeEntity oldPartitionNodeEntity);

    PartitionGraphOperateDto addNodeToPartition(String nodeId, PartitionNodeEntity targetPartitionNodeEntity);
}
