package cn.edu.nju.software.sda.core.domain.node;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FunctionNode extends Node {
    public FunctionNode(String name) {
        super(name);
    }

    @Override
    public String getAttrStr() {
        return "";
    }

    @Override
    public void setAttrStr(String attrStr) {

    }

}
