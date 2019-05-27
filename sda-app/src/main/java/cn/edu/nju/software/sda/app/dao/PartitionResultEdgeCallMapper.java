package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.PartitionNodeEdgePairEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PartitionResultEdgeCallMapper extends Mapper<PartitionNodeEdgePairEntity> {

}