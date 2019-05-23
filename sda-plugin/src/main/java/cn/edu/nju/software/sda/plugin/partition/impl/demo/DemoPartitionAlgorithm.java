package cn.edu.nju.software.sda.plugin.partition.impl.demo;

import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithm;

import java.io.File;

public class DemoPartitionAlgorithm implements PartitionAlgorithm {

    @Override
    public String getDesc() {
        return "Demo";
    }

    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public boolean match(InputData inputData) {
        return true;
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
