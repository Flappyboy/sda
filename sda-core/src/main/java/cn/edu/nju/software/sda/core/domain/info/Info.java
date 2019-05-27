package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.domain.Base;
import lombok.*;

/**
 * Info中的数据分为两部分：
 *     Profile: 指Info类以及其父类Base中的属性值
 *     Detail：指的是Info的子类中的数据
 */
@Getter
@Setter
@ToString
public abstract class Info extends Base {

    private String name;

    private String desc;

    private String parentId;

    private InfoStatus status;

    public Info(String name) {
        this.name = name;
    }

    public static enum InfoStatus {
        SAVING(),//0
        COMPLETE();//1
    }
}
