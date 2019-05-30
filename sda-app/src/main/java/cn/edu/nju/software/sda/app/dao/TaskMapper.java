package cn.edu.nju.software.sda.app.dao;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.core.domain.Task.Task;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TaskMapper extends Mapper<TaskEntity> {
    List<TaskEntity> queryTask(TaskEntity taskEntity);
}