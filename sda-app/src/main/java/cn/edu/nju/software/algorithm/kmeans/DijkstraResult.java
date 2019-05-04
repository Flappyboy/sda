package cn.edu.nju.software.algorithm.kmeans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DijkstraResult {
    private String sourceData;
    private int sourceId;
    private String targetData;
    private int targetId;
    private double weigth;
}
