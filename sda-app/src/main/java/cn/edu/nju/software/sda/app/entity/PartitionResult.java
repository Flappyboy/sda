package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class PartitionResult implements Serializable {

    @Id
    private String id;

    @Column(name = "`desc`")
    private String desc;

    @Column(name = "`name`")
    private String name;

    private String algorithmsId;

    private String dynamicAnalysisInfoId;

    private String appId;

    private Date createdAt;

    private Date updatedAt;

    private Integer flag;

    private Integer type;

    @Column(name = "`order`")
    private Integer order;

    private String partitionId;

    private static final long serialVersionUID = 1L;

    @Transient
    private String typeName;

    public String getTypeName() {
        if(type==0)
            return "Class";
        else
            return  "Method";
    }
}