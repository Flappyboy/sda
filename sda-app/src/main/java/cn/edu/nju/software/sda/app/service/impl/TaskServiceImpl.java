package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.PartitionPairMapper;
import cn.edu.nju.software.sda.app.dao.TaskDataMapper;
import cn.edu.nju.software.sda.app.dao.TaskMapper;
import cn.edu.nju.software.sda.app.entity.PartitionPairEntity;
import cn.edu.nju.software.sda.app.entity.TaskDataEntity;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.app.service.PartitionNodeEdgeService;
import cn.edu.nju.software.sda.app.service.TaskService;
import cn.edu.nju.software.sda.core.InfoNameManager;
import cn.edu.nju.software.sda.core.dao.InfoManager;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.Task.Task;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import cn.edu.nju.software.sda.core.domain.info.PartitionInfo;
import cn.edu.nju.software.sda.core.domain.meta.DataType;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.exception.WorkFailedException;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskDataMapper taskDataMapper;

    @Autowired
    private PartitionPairMapper partitionPairMapper;

    @Autowired
    private PartitionNodeEdgeService partitionNodeEdgeService;

    @Override
    public TaskEntity redo(String taskId) {
        TaskEntity taskEntity = queryTaskEntityById(taskId);
        taskEntity.setId(null);
        taskEntity.getInputDataDto();
        taskEntity.setTaskDataList(null);
        return newTask(taskEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TaskEntity saveOrUpdate(TaskEntity taskEntity) {
        taskEntity.setUpdatedAt(new Date());
        if(StringUtils.isBlank(taskEntity.getId())){
            taskEntity.setId(Sid.nextShort());
            taskEntity.setCreatedAt(new Date());
            taskEntity.setStatus(Task.Status.Draft.name());
            taskEntity.setFlag(1);
            taskMapper.insert(taskEntity);
        }else{
            taskMapper.updateByPrimaryKeySelective(taskEntity);
        }

        Example example = new Example(TaskDataEntity.class);
        TaskDataEntity demoData = new TaskDataEntity();
        demoData.setTaskId(taskEntity.getId());
        example.createCriteria().andEqualTo(demoData);
        taskDataMapper.deleteByExample(example);

        List<TaskDataEntity> taskDataEntities = taskEntity.getTaskDataList();
        for (TaskDataEntity taskData :
                taskDataEntities) {
            taskData.setId(Sid.nextShort());
            taskData.setTaskId(taskEntity.getId());
        }
        if(taskDataEntities.size()>0)
            taskDataMapper.insertList(taskDataEntities);

        return taskEntity;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public TaskEntity newTask(TaskEntity taskEntity) {

        taskEntity = saveOrUpdate(taskEntity);

        TaskEntity finalTaskEntity = taskEntity;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doTask(finalTaskEntity);
            }
        }, taskEntity.getId());
        taskEntity.setStartTime(new Date());
        taskEntity.setStatus(Task.Status.Doing.name());
        taskEntity = saveOrUpdate(taskEntity);

        thread.start();
        return taskEntity;
    }

    @Override
    public void doTask(TaskEntity taskEntity) {
        System.out.println(taskEntity);
        taskEntity = queryTaskEntityById(taskEntity.getId());
        if(taskEntity == null){
            throw new RuntimeException("Query taskEntity is null. "+taskEntity);
        }
        File workspace = WorkspaceUtil.tmp_workspace("task_"+taskEntity.getType());
        Work work = new Work();
        work.setWorkspace(workspace);
        InfoSet infoSet = null;
        InputData inputData = taskEntity.getInputDataDto().toInputData();
        Task.Status status = Task.Status.Complete;
        try {
            infoSet = PluginFunctionManager.get(taskEntity.getFunctionName()).work(inputData, work);

            if(infoSet!=null){
                String parentId = taskEntity.getAppId();

                //如果为评估算法，则其parentId为其中的partitionInfo的Id
                if(taskEntity.getType().equals(FunctionType.Evaluation.name())){
                    for(Map.Entry<String, List<Info>> entry : inputData.getInfoDatas().entrySet()){
                        if (Partition.INFO_NAME_PARTITION.equals(entry.getKey())){
                            parentId = entry.getValue().get(0).getId();
                            break;
                        }
                    }
                }

                List<Info> infoList = infoSet.getInfoList();
                InfoManager.save(parentId, infoList);
                for(Info info: infoList){
                    if(info instanceof PartitionInfo){
                        if(taskEntity != null){
                            List<TaskDataEntity> taskDataEntities = taskEntity.getTaskDataList();
                            for (TaskDataEntity tde :
                                    taskDataEntities) {
                                if(!DataType.InfoData.name().equals(tde.getType())){
                                    continue;
                                }
                                Class clazz = InfoNameManager.getClassByName(tde.getDataType());
                                if(clazz != null && clazz.equals(PairRelationInfo.class)){
                                    String pairId = tde.getData();
                                    PartitionPairEntity partitionPairEntity = new PartitionPairEntity();
                                    partitionPairEntity.setId(Sid.nextShort());
                                    partitionPairEntity.setPartitionInfoId(info.getId());
                                    partitionPairEntity.setPairRelationInfoId(pairId);
                                    partitionPairMapper.insert(partitionPairEntity);
                                }
                            }
                            partitionNodeEdgeService.statisticsPartitionResultEdge(info.getId());
                        }
                    }
                }
                taskEntity.getTaskDataList().addAll(TaskDataEntity.create(infoSet, false, taskEntity.getId()));
                saveOrUpdate(taskEntity);
            }
            FileUtil.delete(workspace);
        } catch (WorkFailedException e) {
            e.printStackTrace();
            status = Task.Status.Error;
        } catch (Exception e){
            e.printStackTrace();
            status = Task.Status.Error;
        }
        taskEntity.setStatus(status.name());
        taskEntity.setEndTime(new Date());
        saveOrUpdate(taskEntity);
    }

    @Override
    public TaskEntity queryTaskEntityById(String taskId) {

        TaskEntity taskEntity = taskMapper.selectByPrimaryKey(taskId);
        if(taskEntity == null){
            return null;
        }
        taskEntity.setTaskDataList(queryTaskDataEntityByTaskId(taskId));
        return taskEntity;
    }

    @Override
    public PageQueryDto<TaskEntity> queryTaskPaged(TaskEntity taskEntity, PageQueryDto<TaskEntity> dto) {
        Page page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), true);

        List<TaskEntity> taskEntities = taskMapper.queryTask(taskEntity);

        return dto.addResult(taskEntities, page.getTotal());
    }

    @Override
    public List<TaskEntity> queryTaskEntityByAppId(String appId) {
        Example example = new Example(TaskEntity.class);
        TaskEntity dataDemo = new TaskEntity();
        dataDemo.setAppId(appId);
        example.createCriteria().andEqualTo(dataDemo);
        List<TaskEntity> taskEntities = taskMapper.selectByExample(example);
        for (TaskEntity taskEntity :
                taskEntities) {
            taskEntity.setTaskDataList(queryTaskDataEntityByTaskId(taskEntity.getId()));
        }
        return taskEntities;
    }

    @Override
    public List<TaskDataEntity> queryTaskDataEntityByTaskId(String taskId) {
        Example example = new Example(TaskDataEntity.class);
        TaskDataEntity dataDemo = new TaskDataEntity();
        dataDemo.setTaskId(taskId);
        example.createCriteria().andEqualTo(dataDemo);
        return taskDataMapper.selectByExample(example);
    }

    @Override
    public TaskEntity queryTaskByOutputInfoId(String infoId) {
        Example example = new Example(TaskDataEntity.class);
        TaskDataEntity dataDemo = new TaskDataEntity();
        dataDemo.setType(DataType.InfoData.name());
        dataDemo.setInput(false);
        dataDemo.setData(infoId);
        example.createCriteria().andEqualTo(dataDemo);
        List<TaskDataEntity> taskDataEntities = taskDataMapper.selectByExample(example);
        if(taskDataEntities.size() == 0){
            return null;
        }
        TaskEntity taskEntity = taskMapper.selectByPrimaryKey(taskDataEntities.get(0).getTaskId());
        taskEntity.setTaskDataList(queryTaskDataEntityByTaskId(taskEntity.getId()));
        return taskEntity;
    }

    @Override
    public InputData queryInputDataByTaskId(String taskId) {
        return queryTaskEntityById(taskId).getInputDataDto().toInputData();
    }


}
