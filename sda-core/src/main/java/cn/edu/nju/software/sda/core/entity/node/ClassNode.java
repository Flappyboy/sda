package cn.edu.nju.software.sda.core.entity.node;

import cn.edu.nju.software.sda.core.exception.UnexpectedTypeException;
import lombok.*;

@Getter
@Setter
@ToString
public class ClassNode extends Node {

    public ClassNode(String name, Type type) {
        super(name, type);
    }

    public enum ClassNodeType implements Type{
        NORMAL("Normal"),
        ABSTRACT("Abstract"),
        INTERFACE("Interface");

        private String name;

        ClassNodeType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private NodeSet<MethodNode> methodNodeSet;

    public void setType(Type type){
        if(type==null){
            throw new NullPointerException();
        }

        for(ClassNodeType methodNodeType : ClassNodeType.values()){
            if(type.equals(methodNodeType)){
                super.setType(type);
            }
        }
        throw new UnexpectedTypeException(type);
    }
}
