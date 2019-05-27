package cn.edu.nju.software.sda.core.domain.node;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClassNode extends Node {

    private Type type = Type.NORMAL;

    public ClassNode(String name) {
        super(name);
    }

    @Override
    public String getAttrStr() {
        return type.name();
    }

    @Override
    public void setAttrStr(String attrStr) {
        this.setType(Type.valueOf(attrStr));
    }

    public enum Type {
        NORMAL(),
        ABSTRACT(),
        INTERFACE();
    }
}
