package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.ClassNode;
import cn.edu.nju.software.sda.app.entity.ClassNodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassNodeMapper {
    int countByExample(ClassNodeExample example);

    int deleteByExample(ClassNodeExample example);

    int deleteByPrimaryKey(String id);

    int insert(ClassNode record);

    int insertSelective(ClassNode record);

    List<ClassNode> selectByExample(ClassNodeExample example);

    ClassNode selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ClassNode record, @Param("example") ClassNodeExample example);

    int updateByExample(@Param("record") ClassNode record, @Param("example") ClassNodeExample example);

    int updateByPrimaryKeySelective(ClassNode record);

    int updateByPrimaryKey(ClassNode record);
}