package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.domain.Base;
import lombok.*;

@Getter
@Setter
@ToString
public abstract class Info extends Base {

    private String name;

    private String desc;

    private String appId;

    public Info(String name) {
        this.name = name;
    }
}
