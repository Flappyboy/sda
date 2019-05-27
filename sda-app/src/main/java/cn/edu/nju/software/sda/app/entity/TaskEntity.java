package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.n3r.idworker.Sid;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

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

    private Long threadId;

    @Column(name = "`desc`")
    private String desc;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    @Transient
    private InputData inputData;

    @Transient
    private InfoSet outPutInfo;

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
}