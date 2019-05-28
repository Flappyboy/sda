package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.PartitionPairEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PartitionPairMapper extends Mapper<PartitionPairEntity>, InsertListMapper<PartitionPairEntity> {
}