package cn.edu.nju.software.sda.app.entity.adapter;

import cn.edu.nju.software.sda.core.entity.node.ClassNode;

public class ClassNodeAdapter extends ClassNode {
    public static final Class clazz = ClassNode.class;
    public ClassNodeAdapter(cn.edu.nju.software.sda.app.entity.ClassNode classNode) {
        setId(classNode.getId());
        setName(classNode.getName());
    }
}
