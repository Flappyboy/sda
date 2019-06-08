package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.PartitionNodeEdgeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PartitionNodeEdgeMapper extends Mapper<PartitionNodeEdgeEntity> {

    List<PartitionNodeEdgeEntity> statisticsEdges(@Param("partitionId") String partitionId, @Param("pairRelationId") String pairRelationId);

    List<PartitionNodeEdgeEntity> queryEdgeByPartitionId(@Param("partitionId") String partitionId);
}