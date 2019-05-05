package cn.edu.nju.software.sda.core.entity.node;

import cn.edu.nju.software.sda.core.exception.UnexpectedTypeException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MethodNode extends Node {

    public enum MethodNodeType implements Type{
        NORMAL("Normal"),
        ABSTRACT("Abstract");

        private String name;

        MethodNodeType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
    public void setType(Type type){
        if(type==null){
            throw new NullPointerException();
        }

        for(MethodNodeType methodNodeType : MethodNodeType.values()){
            if(type.equals(methodNodeType)){
                super.setType(type);
            }
        }
        throw new UnexpectedTypeException(type);
    }
}
