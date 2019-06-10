package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.dao.EvaluationIndicatorMapper;
import cn.edu.nju.software.sda.app.dao.EvaluationInfoMapper;
import cn.edu.nju.software.sda.app.dto.InfoDto;
import cn.edu.nju.software.sda.app.dto.TaskInputDataDto;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.core.domain.Task.Task;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
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
    
    @Autowired
    private PartitionService partitionService;

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
        }else{
            PartitionInfoEntity partitionInfoEntity = partitionService.findPartitionById(partitionId);
            List<PairRelationInfoEntity> pairRelationInfoEntities = partitionInfoEntity.getPairRelationInfoEntityList();
            for (PairRelationInfoEntity prie :
                    pairRelationInfoEntities) {
                if (prie.getName().equals(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT)) {
                    TaskEntity taskEntity = new TaskEntity();
                    taskEntity.setAppId(partitionInfoEntity.getAppId());
                    taskEntity.setFunctionName("SYS_Metric");
                    TaskInputDataDto taskInputDataDto = new TaskInputDataDto();
                    Map<String, List<InfoDto>> infoDatas = new HashMap<>();
                    infoDatas.put(Node.INFO_NAME_NODE, Arrays.asList(new InfoDto(partitionInfoEntity.getAppId(), Node.INFO_NAME_NODE)));
                    infoDatas.put(Partition.INFO_NAME_PARTITION, Arrays.asList(new InfoDto(partitionInfoEntity.getId(), Partition.INFO_NAME_PARTITION)));
                    infoDatas.put(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT, Arrays.asList(new InfoDto(prie.getId(), PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT)));
                    taskInputDataDto.setInfoDatas(infoDatas);
                    taskEntity.setInputDataDto(taskInputDataDto);
                    taskEntity.setType("Evaluation");
                    taskEntity = taskService.newTask(taskEntity);
                    return taskService.waitTask(taskEntity.getId());
                }
            }
        }
        return null;
    }
}
