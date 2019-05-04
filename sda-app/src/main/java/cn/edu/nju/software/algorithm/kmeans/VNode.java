package cn.edu.nju.software.algorithm.kmeans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VNode {
    int ivex;               //顶点位置
    String data;          // 顶点信息
    ENode firstEdge;    // 指向第一条依附该顶点的弧
    int degree;
    double sumWeight;

}
