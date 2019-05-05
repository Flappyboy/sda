package cn.edu.nju.software.sda.core.entity.node;

import lombok.*;

@Getter
@Setter
@ToString
public class FunctionNode extends Node {
    public FunctionNode(String name, Type type) {
        super(name, type);
    }

    private NodeSet<ClassNode> classNodeNodeSet;
}
