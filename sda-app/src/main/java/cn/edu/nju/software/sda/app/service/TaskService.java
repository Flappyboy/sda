package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.PairRelationInfoEntity;
import cn.edu.nju.software.sda.app.entity.TaskDataEntity;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {

    TaskEntity redo(String taskId);

    TaskEntity saveOrUpdate(TaskEntity taskEntity);

    TaskEntity newTask(TaskEntity taskEntity);

    @Transactional(propagation = Propagation.REQUIRED)
    void doTask(TaskEntity taskEntity);

    /**
     * 查询taskEntity
     * @param taskId
     * @return
     */
    TaskEntity queryTaskEntityById(String taskId);

    PageQueryDto<TaskEntity> queryTaskPaged(TaskEntity taskEntity, PageQueryDto<TaskEntity> dto);

    List<TaskEntity> queryTaskEntityByAppId(String appId);

    List<TaskDataEntity> queryTaskDataEntityByTaskId(String taskId);

    TaskEntity queryTaskByOutputInfoId(String infoId);

    InputData queryInputDataByTaskId(String taskId);
}
