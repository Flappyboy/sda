package cn.edu.nju.software.sda.plugin.info;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.plugin.base.SdaPluginFunction;

import java.io.File;
import java.util.List;

public interface InfoCollection extends SdaPluginFunction {

    List<Info> processData(InputData inputData, File workspace);
}
