package cn.edu.nju.software.sda.core.domain.node;

import cn.edu.nju.software.sda.core.Constants;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MethodNode extends Node {

    private Type type = Type.NORMAL;

    private String methodName;

    private String outputClazz;

    private List<String> inputClazz;

//    private List<String> inputName;

    public MethodNode(String name) {
        super(name);
    }

    @Override
    public String getAttrStr() {

        StringBuilder sb = new StringBuilder();
        sb.append(getTypeSb()).append(" "+Constants.SPLIT_semicolon+" ")
                .append(getMethodNameSb()).append(" "+Constants.SPLIT_semicolon+" ")
                .append(getOutputClazzSb()).append(" "+Constants.SPLIT_semicolon+" ")
                .append(getInputClazzSb());
//                .append(getInputNameSb());
        return sb.toString();
    }

    @Override
    public void setAttrStr(String attrStr) {
        if(StringUtils.isBlank(attrStr)){
            attrStr = StringUtils.repeat(" "+Constants.SPLIT_semicolon+" ", 4);
        }
        String[] strs = StringUtils.split(attrStr, Constants.SPLIT_semicolon);

        setType(Type.getTypeByName(StringUtils.stripToNull(strs[0])));
        setMethodName(StringUtils.stripToNull(strs[1]));
        setOutputClazz(StringUtils.stripToNull(strs[2]));
        setInputClazzByStr(StringUtils.stripToNull(strs[3]));
//        setInputNameByStr(StringUtils.stripToNull(strs[4]));
    }

    private StringBuilder getTypeSb(){
        if(type == null){
            return new StringBuilder();
        }
        return new StringBuilder(type.name());
    }

    private StringBuilder getMethodNameSb(){
        if(methodName == null){
            return new StringBuilder();
        }
        return new StringBuilder(methodName);
    }
    private StringBuilder getOutputClazzSb(){
        if(outputClazz == null){
            return new StringBuilder();
        }
        return new StringBuilder(outputClazz);
    }
    private StringBuilder getInputClazzSb(){
        if(inputClazz == null){
            return new StringBuilder();
        }
        return new StringBuilder(StringUtils.join(inputClazz, Constants.SPLIT_comma));
    }

    private void setInputClazzByStr(String inputClazzStr){
        if(StringUtils.isBlank(inputClazzStr)){
            setInputClazz(new ArrayList<>());
            return;
        }
        setInputClazz(Arrays.asList(StringUtils.split(inputClazzStr, Constants.SPLIT_comma)));
    }

//    private StringBuilder getInputNameSb(){
//        if(inputName == null){
//            return new StringBuilder();
//        }
//        return new StringBuilder(StringUtils.join(inputName, Constants.SPLIT_comma));
//    }
//
//    private void setInputNameByStr(String str){
//        if(StringUtils.isBlank(str)){
//            setInputClazz(new ArrayList<>());
//            return;
//        }
//        setInputName(Arrays.asList(StringUtils.split(str, Constants.SPLIT_comma)));
//    }


    public enum Type{
        NORMAL(),
        ABSTRACT();

        public static Type getTypeByName(String name){
            if(StringUtils.isBlank(name)){
                return  null;
            }
            return Type.valueOf(name);
        }
    }
}
