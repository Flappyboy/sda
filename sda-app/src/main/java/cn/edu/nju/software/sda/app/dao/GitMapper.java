package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.Git;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface GitMapper extends Mapper<Git> {
}
