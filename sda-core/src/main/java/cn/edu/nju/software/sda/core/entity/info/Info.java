package cn.edu.nju.software.sda.core.entity.info;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Info {
    private String id;

    private String name;
}
