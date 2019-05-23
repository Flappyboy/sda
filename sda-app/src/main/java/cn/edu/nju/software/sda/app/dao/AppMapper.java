package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AppMapper extends Mapper<AppEntity> {
}