package cn.edu.nju.software.sda.plugin.partition.impl;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithm;

import java.io.File;

public class DemoPartitionAlgorithm implements PartitionAlgorithm {

    @Override
    public boolean match(EffectiveInfo effectiveInfo) {
        return true;
    }

    @Override
    public <N extends Node, P extends Node> Partition<N> partition(App<N, P> app, File workspace) {
        System.out.println("---------------------------haahhaahahhahaahahah=--------------------------------");
        return null;
    }

    @Override
    public String getName() {
        return "Demo";
    }
}
