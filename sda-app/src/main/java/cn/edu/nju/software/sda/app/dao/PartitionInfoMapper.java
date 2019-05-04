package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.PartitionInfo;
import cn.edu.nju.software.sda.app.entity.PartitionInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartitionInfoMapper {
    int countByExample(PartitionInfoExample example);

    int deleteByExample(PartitionInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(PartitionInfo record);

    int insertSelective(PartitionInfo record);

    List<PartitionInfo> selectByExample(PartitionInfoExample example);

    PartitionInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") PartitionInfo record, @Param("example") PartitionInfoExample example);

    int updateByExample(@Param("record") PartitionInfo record, @Param("example") PartitionInfoExample example);

    int updateByPrimaryKeySelective(PartitionInfo record);

    int updateByPrimaryKey(PartitionInfo record);
}