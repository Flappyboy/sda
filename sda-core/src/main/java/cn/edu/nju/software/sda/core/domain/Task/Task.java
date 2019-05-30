package cn.edu.nju.software.sda.core.domain.Task;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class Task implements Serializable {

    private String id;

    private String appId;

    private String type;

    private Status status;

    private String desc;

    private Integer flag;

    private Date createdAt;

    private Date updatedAt;

    public static enum Status{
        Draft(),
        Doing(),
        Warn(),
        Complete(),
        Error(),
        Terminate();
    }
}