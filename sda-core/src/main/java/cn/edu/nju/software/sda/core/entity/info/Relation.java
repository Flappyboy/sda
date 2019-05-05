package cn.edu.nju.software.sda.core.entity.info;

import lombok.Data;

@Data
public abstract class Relation {
    private String id;

    private String name;

    private Double value;

    private Class nodeClass;

    public Relation(Double value, Class nodeClass) {
        this.value = value;
        this.nodeClass = nodeClass;
    }

    public Relation(String id, Double value, Class nodeClass) {
        this.id = id;
        this.value = value;
        this.nodeClass = nodeClass;
    }

    public void addValue(Double v){
        value += v;
    }
}
