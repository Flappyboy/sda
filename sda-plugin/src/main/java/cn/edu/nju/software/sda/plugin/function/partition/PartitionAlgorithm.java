package cn.edu.nju.software.sda.plugin.function.partition;

import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;

public abstract class PartitionAlgorithm implements FunctionService {
    @Override
    public FunctionType getType() {
        return FunctionType.Partition;
    }
}
