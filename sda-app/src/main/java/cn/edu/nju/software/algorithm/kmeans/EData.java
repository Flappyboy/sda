package cn.edu.nju.software.algorithm.kmeans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EData {
    String start; // 边的起点
    String end;   // 边的终点
    double weight; // 边的权重

    public EData(String start, String end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
}
