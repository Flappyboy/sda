package cn.edu.nju.software.sda.app.entity.bean;

import cn.edu.nju.software.sda.app.entity.Algorithms;
import cn.edu.nju.software.sda.app.entity.AlgorithmsParam;

import java.util.List;

public class AlgorithmsBean {
    private Algorithms algorithms;
    private List<AlgorithmsParam> params;

    public void setAlgorithms(Algorithms algorithms) {
        this.algorithms = algorithms;
    }

    public void setParams(List<AlgorithmsParam> params) {
        this.params = params;
    }

    public Algorithms getAlgorithms() {
        return algorithms;
    }

    public List<AlgorithmsParam> getParams() {
        return params;
    }
}
