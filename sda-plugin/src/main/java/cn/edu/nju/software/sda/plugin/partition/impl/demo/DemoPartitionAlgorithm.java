package cn.edu.nju.software.sda.plugin.partition.impl.demo;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithm;

import java.io.File;

public class DemoPartitionAlgorithm implements PartitionAlgorithm {

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
        return "Demo";
    }
}
