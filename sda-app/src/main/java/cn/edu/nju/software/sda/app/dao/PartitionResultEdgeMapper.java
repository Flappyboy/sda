package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.PartitionNodeEdgeEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PartitionResultEdgeMapper extends Mapper<PartitionNodeEdgeEntity> {
    List<PartitionNodeEdgeEntity> statisticsEdgesFromStatic(String partitionId, String appId);

    List<PartitionNodeEdgeEntity> statisticsEdgesFromDynamic(String partitionId, String dynamicAnalysisInfoId);

    List<PartitionNodeEdgeEntity> queryEdgeByPartitionId(String partitionId);
}