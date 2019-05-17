package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.PartitionDetail;
import cn.edu.nju.software.sda.app.entity.PartitionResult;

import java.util.HashMap;
import java.util.List;

public interface PartitionDetailService {
    PartitionDetail savePartitionDetail(PartitionDetail partitionDetail);

    void updatePartitionDetail(PartitionDetail partitionDetail);

    void deletePartitionDetail(String partitionDetailId);

    PartitionDetail queryPartitionDetailById(String partitionDetailId);

    List<HashMap<String, String>> queryPartitionDetailListPaged(String partitionResultId, int type, Integer page, Integer pageSize);

    int countOfPartitionDetail(String partitionResultId, int type);

    List<Object> queryPartitionDetailByResultId(String partitionResultId);

    List<Object> queryPartitionDetailByResultIdPaged(String partitionResultId, Integer page, Integer pageSize);
    int countOfPartitionDetailByResultId(String partitionResultId);

    PartitionGraphOperateDto moveNodeToPartition(String nodeId, PartitionResult oldPartitionResult, PartitionResult targetPartitionResult);

    PartitionGraphOperateDto removeNodeFromPartition(String nodeId, PartitionResult oldPartitionResult);

    PartitionGraphOperateDto addNodeToPartition(String nodeId, PartitionResult targetPartitionResult);
}
