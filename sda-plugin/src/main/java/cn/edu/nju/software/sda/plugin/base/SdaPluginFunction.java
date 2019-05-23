package cn.edu.nju.software.sda.plugin.base;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;

import java.util.List;

public interface SdaPluginFunction {

    /**
     * 功能名，应全局唯一
     * @return
     */
    String getName();

    /**
     * 该功能的描述
     * @return
     */
    String getDesc();

    /**
     * 描述该功能所需的输入
     * @return
     */
    MetaData getMetaData();

    /**
     * 判断输入是否符合要求
     * @param inputData
     * @return
     */
    boolean match(InputData inputData);
}
