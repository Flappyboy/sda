package cn.edu.nju.software.sda.plugin.function.evaluation.impl.metric;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Edge {
    private String sourceName;
    private String targetName;
    private int count;    //int -> double

    public Edge(){

    }

    public Edge(String sourceName, String targetName, int count){
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.count = count;

    }
}
