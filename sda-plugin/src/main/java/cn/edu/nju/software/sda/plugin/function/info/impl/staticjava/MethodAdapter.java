package cn.edu.nju.software.sda.plugin.function.info.impl.staticjava;

import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

public class MethodAdapter extends MethodVisitor implements Opcodes {

    protected String className = null;
    protected int access = -1;
    protected String name = null;
    protected String desc = null;
    protected String signature = null;
    protected String[] exceptions = null;

    private final JavaData data;

    protected String fullName;

    public MethodAdapter(JavaData data, final MethodVisitor mv, final String className, final int access, final String name,
                         final String desc, final String signature, final String[] exceptions) {
        super(ASM6, mv);
        this.className = className;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
        this.data = data;
        this.fullName = NameUtils.methodFullName(className, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
//        System.out.println(this.className + " " + this.name + "   call  " + owner + " " + name);
        if (mv != null) {
            mv.visitMethodInsn(opcode, owner, name, desc, itf);
        }
        Node targetClassNode = data.getNodeSet().getNodeByName(owner);

        if(targetClassNode == null){
            targetClassNode = new ClassNode(owner);
        }

        PairRelation newedge = new PairRelation(1d, data.getNodeSet().getNodeByName(this.className), targetClassNode);
        data.getClassEdges().addRelationByAddValue(newedge);

        String targetMethodFullName = NameUtils.methodFullName(owner, name, desc);
        Node targetMethodNode = data.getNodeSet().getNodeByName(targetMethodFullName);
        if(targetMethodNode == null){
            targetMethodNode = new MethodNode(targetMethodFullName);
        }
        newedge = new PairRelation(1d, data.getNodeSet().getNodeByName(this.fullName), targetMethodNode);
        data.getMethodEdges().addRelationByAddValue(newedge);
    }
}