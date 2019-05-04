package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.Algorithms;
import cn.edu.nju.software.sda.app.entity.AlgorithmsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmsMapper {
    int countByExample(AlgorithmsExample example);

    int deleteByExample(AlgorithmsExample example);

    int deleteByPrimaryKey(String id);

    int insert(Algorithms record);

    int insertSelective(Algorithms record);

    List<Algorithms> selectByExample(AlgorithmsExample example);

    Algorithms selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Algorithms record, @Param("example") AlgorithmsExample example);

    int updateByExample(@Param("record") Algorithms record, @Param("example") AlgorithmsExample example);

    int updateByPrimaryKeySelective(Algorithms record);

    int updateByPrimaryKey(Algorithms record);
}