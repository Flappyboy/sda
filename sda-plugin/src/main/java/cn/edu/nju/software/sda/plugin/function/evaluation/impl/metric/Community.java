package cn.edu.nju.software.sda.plugin.function.evaluation.impl.metric;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Community {
    private String communityId;
    private List<ClassNodeInfo> interfaces;
    private List<ClassNodeInfo> allClasses;
}
