package cn.edu.nju.software.sda.core.domain.evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Indicator {
    String name;

    String value;

    public Indicator(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
