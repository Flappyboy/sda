package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.dao.EvaluationIndicatorMapper;
import cn.edu.nju.software.sda.app.dao.EvaluationInfoMapper;
import cn.edu.nju.software.sda.app.entity.EvaluationIndicatorEntity;
import cn.edu.nju.software.sda.app.entity.EvaluationInfoEntity;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationInfoServiceImpl implements EvaluationInfoService {

    @Autowired
    private EvaluationInfoMapper evaluationInfoMapper;

    @Autowired
    private EvaluationIndicatorMapper evaluationIndicatorMapper;

    @Autowired
    private TaskService taskService;

    @Override
    public EvaluationInfoEntity queryLastEvaluationByPartitionId(String partitionId) {
        Example example = new Example(EvaluationInfoEntity.class);
        EvaluationInfoEntity entity = new EvaluationInfoEntity();
        entity.setPartitionId(partitionId);
        example.createCriteria().andEqualTo(entity);
        example.setOrderByClause("updated_at desc");
        List<EvaluationInfoEntity> entities = evaluationInfoMapper.selectByExample(example);
        if(entities.size() == 0){
            return null;
        }
        entity = entities.get(0);

        Example indicatorExample = new Example(EvaluationIndicatorEntity.class);
        EvaluationIndicatorEntity indicatorEntity = new EvaluationIndicatorEntity();
        indicatorEntity.setInfoId(entity.getId());
        indicatorExample.createCriteria().andEqualTo(indicatorEntity);
        List<EvaluationIndicatorEntity> indicatorEntities = evaluationIndicatorMapper.selectByExample(indicatorExample);
        entity.setIndicators(indicatorEntities);
        return entity;
    }

    @Override
    public String redo(String evaluationId) {
        TaskEntity taskEntity = taskService.queryTaskByOutputInfoId(evaluationId);
        if(taskEntity == null){
            throw new RuntimeException("Can't find the task!!");
        }
        taskEntity = taskService.redo(taskEntity.getId());
        return taskEntity.getId();
    }

    @Override
    public TaskEntity redoLastEvaluationForPartitionId(String partitionId) {
        EvaluationInfoEntity evaluationInfo = queryLastEvaluationByPartitionId(partitionId);
        if(evaluationInfo != null) {
            String taskEntityId = redo(evaluationInfo.getId());
            return taskService.waitTask(taskEntityId);
        }
        return null;
    }
}
