package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.TaskMapper;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.app.service.TaskService;
import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.dao.InfoManager;
import cn.edu.nju.software.sda.core.domain.Task.Task;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.exception.WorkFailedException;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.PluginManager;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public TaskEntity saveOrUpdate(TaskEntity taskEntity) {
        taskEntity.setUpdatedAt(new Date());
        if(StringUtils.isBlank(taskEntity.getId())){
            taskEntity.setId(Sid.nextShort());
            taskEntity.setCreatedAt(new Date());
            taskMapper.insert(taskEntity);
        }else{
            taskMapper.updateByPrimaryKeySelective(taskEntity);
        }
        return null;
    }

    @Override
    public TaskEntity newTask(TaskEntity taskEntity) {

        taskEntity.setCreatedAt(new Date());
        if(StringUtils.isNotBlank(taskEntity.getId())){
            taskEntity = taskMapper.selectByPrimaryKey(taskEntity.getId());
        }else{
            taskEntity.setId(Sid.nextShort());
        }
        taskEntity.setUpdatedAt(new Date());
        taskEntity.setFlag(1);

        TaskEntity finalTaskEntity = taskEntity;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doTask(finalTaskEntity);
            }
        });
        thread.start();
        taskEntity.setStartTime(new Date());
        taskEntity.setThreadId(thread.getId());
        taskEntity.setStatus(Task.Status.Doing.name());
        taskMapper.insert(taskEntity);
        return taskEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void doTask(TaskEntity taskEntity) {
        File workspace = WorkspaceUtil.workspace("task_"+taskEntity.getType());
        Work work = new Work();
        work.setWorkspace(workspace);
        InfoSet infoSet = null;
        taskEntity.setStatus(Task.Status.Complete.name());
        try {
            infoSet = PluginFunctionManager.get(taskEntity.getFunctionName()).work(taskEntity.getInputData(), work);
        } catch (WorkFailedException e) {
            e.printStackTrace();
            taskEntity.setStatus(Task.Status.Error.name());
        } catch (RuntimeException e){
            e.printStackTrace();
            taskEntity.setStatus(Task.Status.Error.name());
        }
        if(infoSet!=null)
            InfoManager.save(taskEntity.getId(), infoSet.getInfoList());
        FileUtil.delete(workspace);

        TaskEntity updateTask = new TaskEntity();
        updateTask.setId(taskEntity.getId());
        updateTask.setEndTime(new Date());
        taskMapper.updateByPrimaryKeySelective(updateTask);
    }

    @Override
    public TaskEntity queryTaskEntityById(String taskId) {
        return taskMapper.selectByPrimaryKey(taskId);
    }
}
