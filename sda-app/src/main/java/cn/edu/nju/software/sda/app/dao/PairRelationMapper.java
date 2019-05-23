package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.PairRelationEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;

@Repository
public interface PairRelationMapper extends Mapper<PairRelationEntity>, InsertListMapper<PairRelationEntity> {

}