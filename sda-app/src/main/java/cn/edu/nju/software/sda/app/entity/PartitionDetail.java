package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class PartitionDetail implements Serializable {

    @Id
    private String id;

    private String nodeId;

    private String patitionResultId;

    private Date createdAt;

    private Date updatedAt;

    private Integer type;

    private Integer flag;

    @Column(name = "`desc`")
    private String desc;
}