package cn.edu.nju.software.sda.plugin.function.info.impl.staticjava;

import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.util.Arrays;

public class ClassAdapter extends ClassVisitor implements Opcodes {

    private String owner;
    private boolean isInterface;

    private final JavaData data;

    private String[] pacakageName = {};

    public ClassAdapter(JavaData data, String packageName) {
        super(ASM6);
        this.data = data;
        if(StringUtils.isNoneBlank(packageName))
            this.pacakageName = packageName.replace(".","/").split(",");
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
        boolean flag = true;
        if(this.pacakageName.length>0) {
            for (String str :
                    this.pacakageName) {
                if (className.startsWith(str)) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                return;
        }

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
        boolean flag = true;
        if(this.pacakageName.length>0) {
            for (String str :
                    this.pacakageName) {
                if (this.owner.startsWith(str)) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                return mv;
        }
        if (!isInterface) {
            mv = new MethodAdapter(data, mv, owner, access, name, desc, signature, exceptions);
        }

        String methodFullName = NameUtils.methodFullName(this.owner, name, desc);
        MethodNode methodNode = new MethodNode(name);
        System.out.println(name+"  "+desc+"  "+signature);
        String descFormate = desc.replace("/", ".")
                .replace("(L", "(")
                .replace(")L",")")
                .replace(";L", ";");
        int index1 = descFormate.indexOf("(");
        int index2 = descFormate.lastIndexOf(")");
        if(index1!=-1&&index2!=-1) {
            String[] inputClazzs = descFormate
                    .substring(index1 + 1, index2).split(";");
            String outputClazz = descFormate.substring(index2 + 1);
            if(outputClazz.endsWith(";"))
                outputClazz = outputClazz.substring(0,outputClazz.length()-1);
//            System.out.println(inputClazzs+"  "+outputClazz);
            methodNode.setInputClazz(Arrays.asList(inputClazzs));//输入类型
            methodNode.setOutputClazz(outputClazz);//输出类型
        }
        methodNode.setMethodName(name);
        methodNode.setName(methodFullName);
        methodNode.setParentNode(data.getNodeSet().getNodeByName(this.owner));
        data.getNodeSet().addNode(methodNode);
        return mv;
    }
}