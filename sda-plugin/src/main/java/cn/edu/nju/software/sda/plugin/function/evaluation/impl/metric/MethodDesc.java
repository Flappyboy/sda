package cn.edu.nju.software.sda.plugin.function.evaluation.impl.metric;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class MethodDesc {
    private int paramCount; //参数个数
    private int retCount; // 返回值个数，0-void

    private String methodName;//方法名
    private List<String> param;//参数列表
    private String retType;//返回值
}
