package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.GroupRelationEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface GroupRelationMapper extends Mapper<GroupRelationEntity>, InsertListMapper<GroupRelationEntity> {

}