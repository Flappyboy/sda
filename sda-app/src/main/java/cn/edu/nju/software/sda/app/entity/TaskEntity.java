package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.app.plugin.Generate;
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
@Table(name = "app")
public class TaskEntity implements Serializable {

    @Id
    private String id;

    private String appId;

    private String type;

    private String status;

    private Long threadId;

    private String desc;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    public static TaskEntity createNewInstance(String appId, String type){
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setAppId(Sid.nextShort());
        taskEntity.setAppId(appId);
        return taskEntity;
    }
}