package cn.edu.nju.software.sda.app.entity.bean;

import lombok.*;

@Getter
@Setter
@ToString
public class PartitionNodeEdge {
    private String caller;
    private String callerName;
    private String callerFullName;
    private String callee;
    private String calleeName;
    private String calleeFullName;
    private int count;
}
