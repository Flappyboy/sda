package cn.edu.nju.software.sda.core.entity.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Node {
    private String id;

    @EqualsAndHashCode.Include
    private String name;

    private Type type;
}
