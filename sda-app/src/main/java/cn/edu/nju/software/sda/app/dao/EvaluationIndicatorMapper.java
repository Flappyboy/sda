package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.EvaluationIndicatorEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface EvaluationIndicatorMapper extends Mapper<EvaluationIndicatorEntity>, InsertListMapper<EvaluationIndicatorEntity> {
}