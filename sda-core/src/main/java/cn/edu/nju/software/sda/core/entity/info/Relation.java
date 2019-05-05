package cn.edu.nju.software.sda.core.entity.info;

import lombok.Data;

@Data
public abstract class Relation {
    private String id;

    private String name;

    private Long value;

    private Class nodeClass;

    public Relation(String id, String name, Long value, Class nodeClass) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.nodeClass = nodeClass;
    }
}
