package cn.edu.nju.software.sda.plugin.function.partition;

import cn.edu.nju.software.sda.plugin.function.FunctionType;
import cn.edu.nju.software.sda.plugin.function.PluginFunction;

public abstract class PartitionAlgorithm implements PluginFunction {
    @Override
    public FunctionType getType() {
        return FunctionType.Partition;
    }
}
