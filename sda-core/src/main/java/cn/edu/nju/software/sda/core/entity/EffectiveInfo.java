package cn.edu.nju.software.sda.core.entity;

import cn.edu.nju.software.sda.core.entity.info.Info;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EffectiveInfo {

    private Class targetNodeClass;

    private Set<Class> effectiveNodeClass;

    private Set<Info> effectiveInfo;
}
