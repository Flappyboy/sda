package cn.edu.nju.software.sda.plugin.function.info;

import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;

public abstract class InfoCollection implements FunctionService {
    @Override
    public FunctionType getType(){
        return FunctionType.InfoCollection;
    }
}
