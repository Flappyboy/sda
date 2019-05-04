package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.ClassNode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassNodeMapperExtend {
    int insertBatch(List<ClassNode> classNodeList);
}