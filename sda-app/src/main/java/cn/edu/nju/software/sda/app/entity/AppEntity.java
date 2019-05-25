package cn.edu.nju.software.sda.app.entity;

import cn.edu.nju.software.sda.app.plugin.Generate;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "app")
public class AppEntity implements Serializable {

    @Id
    private String id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`desc`")
    private String desc;

    private Date createdAt;

    private Date updatedAt;

    private Integer nodeCount;

    private Integer flag = 1;

    private Integer status;

    @Transient
    private Integer pinpointPluginStatus;

    public static AppEntity createByNameAndDesc(String name, String desc){
        AppEntity appEntity = new AppEntity();
        appEntity.setName(name);
        appEntity.setDesc(desc);
        return appEntity;
    }

    public Integer getPinpointPluginStatus() {
        if(pinpointPluginStatus!=null) {
            return pinpointPluginStatus;
        }else if(name != null && id!=null) {
            Generate generate = new Generate(name, id);
            if (generate.getJar()!=null && generate.getJar().exists()) {
                return 1;
            }
        }
        return 0;
    }
}