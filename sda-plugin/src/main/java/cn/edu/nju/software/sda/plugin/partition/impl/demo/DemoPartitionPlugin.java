package cn.edu.nju.software.sda.plugin.partition.impl.demo;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;

import java.io.File;

public class DemoPartitionPlugin implements PartitionPlugin {

    @Override
    public String getDesc() {
        return "Demo";
    }

    @Override
    public boolean match(EffectiveInfo effectiveInfo) {
        return false;
    }

    @Override
    public Partition partition(App app, File workspace) {
        System.out.println("---------------------------haahhaahahhahaahahah=--------------------------------");
        return null;
    }

    @Override
    public String getName() {
        return "SYS_P_Demo";
    }
}
