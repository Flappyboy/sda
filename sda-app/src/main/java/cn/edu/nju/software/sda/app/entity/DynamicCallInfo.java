package cn.edu.nju.software.sda.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class DynamicCallInfo implements Serializable {

    @Id
    private String id;

    private String caller;

    private String callee;

    private Integer count;

    private String dynamicAnalysisInfoId;

    private Date createdAt;

    private Date updatedAt;

    private Integer isInclude;

    private Integer flag;

    private Integer type;

    @Column(name = "`desc`")
    private String desc;

    private Object callerObj;
    private Object calleeObj;

}