package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.app.dto.InfoDto;
import cn.edu.nju.software.sda.app.dto.TaskInputDataDto;
import cn.edu.nju.software.sda.core.domain.meta.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.n3r.idworker.Sid;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@Table(name = "task")
public class TaskEntity implements Serializable {

    @Id
    private String id;

    private String appId;

    private String type;

    private String pluginName;

    private String functionName;

    @Column(name = "`status`")
    private String status;

    private Date startTime;

    private Date endTime;

    @Column(name = "`desc`")
    private String desc;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private String appName;

    @Transient
    private TaskInputDataDto inputDataDto;

    @Transient
    private List<TaskDataEntity> taskDataList;

    public static TaskEntity createNewInstance(String appId, String type){
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(Sid.nextShort());
        taskEntity.setAppId(appId);
        taskEntity.setType(type);
        taskEntity.setFlag(1);
        taskEntity.setCreatedAt(new Date());
        taskEntity.setUpdatedAt(new Date());
        return taskEntity;
    }

    public List<TaskDataEntity> getTaskDataList() {
        if(this.taskDataList != null){
            return this.taskDataList;
        }
        if(inputDataDto == null){
            return null;
        }
        List<TaskDataEntity> taskDataEntities = new ArrayList<>();
        Map<String, List<String>> formDatas = inputDataDto.getFormDatas();
        Map<String, List<InfoDto>> infoDatas = inputDataDto.getInfoDatas();
        for (Map.Entry<String, List<String>> entry :
                formDatas.entrySet()) {
            for(String value : entry.getValue()){
                TaskDataEntity taskData = new TaskDataEntity();
                taskData.setTaskId(this.getId());
                taskData.setInput(true);
                taskData.setType(DataType.FormData.name());
                taskData.setName(entry.getKey());
                taskData.setData(value);
                taskDataEntities.add(taskData);
            }
        }
        for (Map.Entry<String, List<InfoDto>> entry :
                infoDatas.entrySet()) {
            for(InfoDto value : entry.getValue()){
                TaskDataEntity taskData = new TaskDataEntity();
                taskData.setTaskId(this.getId());
                taskData.setInput(true);
                taskData.setType(DataType.InfoData.name());
                taskData.setName(entry.getKey());
                taskData.setDataType(value.getName());
                taskData.setData(value.getId());
                taskDataEntities.add(taskData);
            }
        }
        this.taskDataList = taskDataEntities;
        return this.taskDataList;
    }

    public TaskInputDataDto getInputDataDto() {
        if(inputDataDto != null){
            return inputDataDto;
        }
        if(taskDataList == null){
            return null;
        }
        TaskInputDataDto taskInputDataDto = new TaskInputDataDto();
        Map<String, List<String>> formMap = new HashMap<>();
        Map<String, List<InfoDto>> infoMap = new HashMap<>();
        taskInputDataDto.setFormDatas(formMap);
        taskInputDataDto.setInfoDatas(infoMap);

        for (TaskDataEntity data :
                taskDataList) {
            if(!data.getInput()){
                continue;
            }
            switch (DataType.valueOf(data.getType())){
                case FormData:
                    List<String> list = formMap.get(data.getName());
                    if(list == null){
                        list = new ArrayList<>();
                        formMap.put(data.getName(), list);
                    }
                    list.add(data.getData());
                    break;
                case InfoData:
                    List<InfoDto> infoDtos = infoMap.get(data.getName());
                    if(infoDtos == null){
                        infoDtos = new ArrayList<>();
                        infoMap.put(data.getName(), infoDtos);
                    }
                    InfoDto infoDto = new InfoDto();
                    infoDto.setName(data.getDataType());
                    infoDto.setId(data.getData());
                    infoDtos.add(infoDto);
                    break;
                default:
                    throw new RuntimeException("wrong type: "+data.getType());
            }
        }
        this.inputDataDto = taskInputDataDto;
        return this.inputDataDto;
    }
}