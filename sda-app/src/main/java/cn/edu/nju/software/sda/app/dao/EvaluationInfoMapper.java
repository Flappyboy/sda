package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.entity.EvaluationInfoEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface EvaluationInfoMapper extends Mapper<EvaluationInfoEntity> {
}