package cn.edu.nju.software.sda.plugin.function.info;

import cn.edu.nju.software.sda.plugin.function.FunctionType;
import cn.edu.nju.software.sda.plugin.function.PluginFunction;

public abstract class InfoCollection implements PluginFunction {
    @Override
    public FunctionType getType(){
        return FunctionType.InfoCollection;
    }
}
