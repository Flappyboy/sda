package cn.edu.nju.software.sda.app.entity.adapter;

import cn.edu.nju.software.sda.core.entity.node.ClassNode;
import cn.edu.nju.software.sda.core.entity.node.Type;

public class ClassNodeAdapter extends ClassNode {
    public static final Class clazz = ClassNode.class;
    public ClassNodeAdapter(cn.edu.nju.software.sda.app.entity.ClassNode classNode, Type type) {
        super(classNode.getName(), type);
        setId(classNode.getId());
    }
}
