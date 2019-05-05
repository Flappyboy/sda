package cn.edu.nju.software.sda.core.entity;

import cn.edu.nju.software.sda.core.entity.info.Info;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class EffectiveInfo {

    private Class targetNodeClass;

    private Set<Class> effectiveNodeClass;

    private Set<Info> effectiveInfo;
}
