package cn.edu.nju.software.sda.plugin.partition;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import cn.edu.nju.software.sda.plugin.base.SdaPlugin;

import java.io.File;
import java.io.IOException;

public interface PartitionAlgorithm extends SdaPlugin {

    boolean match(EffectiveInfo effectiveInfo);

    Partition partition(App app, File workspace) throws IOException;
}
