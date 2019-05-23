package cn.edu.nju.software.sda.plugin.info.impl.staticjava;

import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

public class ClassAdapter extends ClassVisitor implements Opcodes {

    private String owner;
    private boolean isInterface;

    private final JavaData data;

    public ClassAdapter(JavaData data) {
        super(ASM6);
        this.data = data;
    }

    // 该方法是当扫描类时第一个拜访的方法，主要用于类声明使用：visit( 类版本 ,修饰符 , 类名 , 泛型信息 , 继承的父类 , 实现的接口)
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (cv != null) {
            cv.visit(version, access, name, signature, superName, interfaces);

        }
        owner = name;
        String className = owner;
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
//        System.out.println(name);
        ClassNode cnode = (ClassNode) data.getNodeSet().getNodeByName(className);
        if (cnode == null) {
            ClassNode cnode1 = new ClassNode(className);
            if (isInterface) {
                cnode1.setType(ClassNode.Type.INTERFACE);
            }else
                cnode1.setType(ClassNode.Type.NORMAL);
            data.getNodeSet().addNode(cnode1);
        }
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
                                     final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (!isInterface) {
            mv = new MethodAdapter(data, mv, owner, access, name, desc, signature, exceptions);
        }

        String methodFullName = NameUtils.methodFullName(this.owner, name, desc);
        MethodNode methodNode = new MethodNode(name);
        methodNode.setName(methodFullName);
        methodNode.setParentNode(data.getNodeSet().getNodeByName(this.owner));
        data.getNodeSet().addNode(methodNode);
        return mv;
    }
}