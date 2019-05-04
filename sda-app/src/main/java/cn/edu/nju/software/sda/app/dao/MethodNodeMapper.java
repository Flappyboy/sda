package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.MethodNode;
import cn.edu.nju.software.sda.app.entity.MethodNodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodNodeMapper {
    int countByExample(MethodNodeExample example);

    int deleteByExample(MethodNodeExample example);

    int deleteByPrimaryKey(String id);

    int insert(MethodNode record);

    int insertSelective(MethodNode record);

    List<MethodNode> selectByExampleWithBLOBs(MethodNodeExample example);

    List<MethodNode> selectByExample(MethodNodeExample example);

    MethodNode selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MethodNode record, @Param("example") MethodNodeExample example);

    int updateByExampleWithBLOBs(@Param("record") MethodNode record, @Param("example") MethodNodeExample example);

    int updateByExample(@Param("record") MethodNode record, @Param("example") MethodNodeExample example);

    int updateByPrimaryKeySelective(MethodNode record);

    int updateByPrimaryKeyWithBLOBs(MethodNode record);

    int updateByPrimaryKey(MethodNode record);
}