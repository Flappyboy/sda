package cn.edu.nju.software.sda.plugin.function.partition.impl.demo;

import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;

public class DemoPartitionAlgorithm extends PartitionAlgorithm {

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
    public InfoSet work(InputData inputData, Work work) {
        System.out.println("----------ha-----------------");
        return null;
    }


    @Override
    public String getName() {
        return "SYS_P_Demo";
    }
}
