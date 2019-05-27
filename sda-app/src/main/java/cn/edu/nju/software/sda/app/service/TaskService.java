package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.TaskEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TaskService {

    TaskEntity saveOrUpdate(TaskEntity taskEntity);

    TaskEntity newTask(TaskEntity taskEntity);

    @Transactional(propagation = Propagation.REQUIRED)
    void doTask(TaskEntity taskEntity);

    /**
     * 查询taskEntity 没有填充其中的InputData等对象
     * @param taskId
     * @return
     */
    TaskEntity queryTaskEntityById(String taskId);
}
