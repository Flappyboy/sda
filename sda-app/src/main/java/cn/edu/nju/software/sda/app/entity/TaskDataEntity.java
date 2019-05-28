package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.app.dto.TaskInputDataDto;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.meta.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.n3r.idworker.Sid;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "task_data")
public class TaskDataEntity implements Serializable {

    @Id
    private String id;

    private String taskId;

    // 若input为true, 表示输入的标志
    @Column(name = "`name`")
    private String name;

    // 表示是否为输入数据
    private Boolean input;

    // DataType.FormData, infoData
    private String type;

    // form: FILE, STRING, TIMESTAMP....
    // info: infoName
    private String dataType;

    private String data;

    public static TaskDataEntity create(Info info, Boolean input, String taskId){
        TaskDataEntity taskDataEntity = new TaskDataEntity();
        taskDataEntity.setId(Sid.nextShort());
        taskDataEntity.setTaskId(taskId);
        taskDataEntity.setType(DataType.InfoData.name());
        taskDataEntity.setDataType(info.getName());
        taskDataEntity.setData(info.getId());
        taskDataEntity.setInput(input);
        return taskDataEntity;
    }

    public static List<TaskDataEntity> create(InfoSet infoSet, Boolean input, String taskId){
        List<TaskDataEntity> list = new ArrayList<>();
        for (Info info :
                infoSet.getInfoList()) {
            list.add(create(info, input, taskId));
        }
        return list;
    }
}