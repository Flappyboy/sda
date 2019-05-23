package cn.edu.nju.software.sda.plugin.partition;

import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.EffectiveInfo;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.plugin.base.SdaPluginFunction;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PartitionAlgorithm extends SdaPluginFunction {

    Partition partition(App app, File workspace) throws IOException;
}
