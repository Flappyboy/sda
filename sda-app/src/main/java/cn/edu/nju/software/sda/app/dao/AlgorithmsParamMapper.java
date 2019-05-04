package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.AlgorithmsParam;
import cn.edu.nju.software.sda.app.entity.AlgorithmsParamExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmsParamMapper {
    int countByExample(AlgorithmsParamExample example);

    int deleteByExample(AlgorithmsParamExample example);

    int deleteByPrimaryKey(String id);

    int insert(AlgorithmsParam record);

    int insertSelective(AlgorithmsParam record);

    List<AlgorithmsParam> selectByExample(AlgorithmsParamExample example);

    AlgorithmsParam selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AlgorithmsParam record, @Param("example") AlgorithmsParamExample example);

    int updateByExample(@Param("record") AlgorithmsParam record, @Param("example") AlgorithmsParamExample example);

    int updateByPrimaryKeySelective(AlgorithmsParam record);

    int updateByPrimaryKey(AlgorithmsParam record);
}