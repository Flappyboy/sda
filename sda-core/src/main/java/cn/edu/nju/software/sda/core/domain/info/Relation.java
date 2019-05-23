package cn.edu.nju.software.sda.core.domain.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class Relation {
    private String id;

    private String name;

    private Double value;

    public static enum Direction {
        FORWORD("FORWORD"),
        REVERSE("REVERSE"),
        NONE("NONE")
        ;
        private String name;

        Direction(String name) {
            this.name = name;
        }
    }

    public Relation(Double value) {
        this.value = value;
    }

    public Relation(String id, Double value) {
        this.id = id;
        this.value = value;
    }

    public void addValue(Double v){
        value += v;
    }
}
