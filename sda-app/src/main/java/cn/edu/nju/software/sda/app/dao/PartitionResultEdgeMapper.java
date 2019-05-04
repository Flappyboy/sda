package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.PartitionResultEdge;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PartitionResultEdgeMapper extends Mapper<PartitionResultEdge> {
    List<PartitionResultEdge> statisticsEdgesFromStatic(String partitionId, String appId);

    List<PartitionResultEdge> statisticsEdgesFromDynamic(String partitionId, String dynamicAnalysisInfoId);

    List<PartitionResultEdge> queryEdgeByPartitionId(String partitionId);
}