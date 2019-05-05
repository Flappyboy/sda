package cn.edu.nju.software.sda.core.entity.node;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FunctionNode extends Node {

    private NodeSet<ClassNode> classNodeNodeSet;
}
