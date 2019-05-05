package cn.edu.nju.software.sda.app.entity.bean;

import cn.edu.nju.software.sda.app.entity.ClassNode;
import cn.edu.nju.software.sda.app.entity.MethodNode;
import cn.edu.nju.software.sda.app.entity.PartitionResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PartitionGraphNode {
    private PartitionResult community;
    private List<ClassNode> classNodes;
    private List<MethodNode> methodNodes;
    private int claaSize = 0;
    private int methodSize = 0;

}
