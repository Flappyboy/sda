package cn.edu.nju.software.sda.app.utils.asm;

import cn.edu.nju.software.sda.app.entity.ClassNode;
import cn.edu.nju.software.sda.app.entity.MethodNode;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class ClassAdapter extends ClassVisitor implements Opcodes {

    private String owner;
    private boolean isInterface;

    private static final ThreadLocal<HashMap<String, ClassNode>> classNodesThreadLocal = new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return new HashMap<String, ClassNode>();
        }
    };

    private static final ThreadLocal<HashMap<String, MethodNode>> methodNodesThreadLocal = new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return new HashMap<String, MethodNode>();
        }
    };

//    public static HashMap<String, ClassNode> classNodes = new HashMap<String, ClassNode>();
//    public static HashMap<String, MethodNode> methodNodes = new HashMap<String, MethodNode>();
    public static int interfaceNum = 0;

    public ClassAdapter() {
        super(ASM6);
    }

    // 该方法是当扫描类时第一个拜访的方法，主要用于类声明使用：visit( 类版本 ,修饰符 , 类名 , 泛型信息 , 继承的父类 , 实现的接口)
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (cv != null) {
            cv.visit(version, access, name, signature, superName, interfaces);

        }
        owner = name;
        String classname = owner
                .replace("/", ".");
//                .replace(";", ",")
//                .replace("(L", "(")
//                .replace(",)", ")")
//                .replace(",L", ",");
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
//        System.out.println(name);
        ClassNode cnode = getClassNodes().get(classname);
        if (cnode == null) {
            ClassNode cnode1 = new ClassNode();
            cnode1.setName(classname);
            if (isInterface) {
                cnode1.setType(1);
                interfaceNum ++;
            }else
                cnode1.setType(0);
            getClassNodes().put(classname, cnode1);
        }
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
                                     final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (!isInterface) {
            mv = new MethodAdapter(mv, owner, access, name, desc, signature, exceptions);
        }

        String methodFullName = (this.owner + "." + name + desc)
                .replace("/", ".")
                .replace(";", ",")
                .replace("(L", "(")
                .replace(",)", ")")
                .replace(",L", ",")
                .replace(")L",")");
//        int index = methodName.lastIndexOf(")");
//        methodName = methodName.substring(0, index + 1);

//        String className = methodName.substring(0, index + 1);
        String className = this.owner.replace("/", ".");
        String methodName =(name+ desc)
                .replace("/", ".")
                .replace(";", ",")
                .replace("(L", "(")
                .replace(",)", ")")
                .replace(",L", ",")
                .replace(")L",")");
        MethodNode methodNode = new MethodNode();
        methodNode.setName(name);
        methodNode.setClassname(className);
        methodNode.setFullname(methodFullName);
//        methodNode.setClassid(className);
        getMethodNodes().put(methodFullName, methodNode);
        return mv;
    }

    public static Map<String, ClassNode> getClassNodes(){
        return classNodesThreadLocal.get();
    }
    public static Map<String, MethodNode> getMethodNodes(){
        return methodNodesThreadLocal.get();
    }

    public static void main(String[] args){
        String a = "Classnode.setId(java.(lang.Integer)";
        System.out.println(a.indexOf("("));
    }
}