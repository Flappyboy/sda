package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "partition_info")
public class PartitionInfoEntity implements Serializable {

    @Id
    private String id;

    private String appId;

    private String dynamicAnalysisinfoId;

    private String algorithmsId;

    @Column(name = "`desc`")
    private String desc;

    private Integer status;

    private Integer flag;

    private Integer type;

    private Date createdAt;

    private Date updatedAt;
}