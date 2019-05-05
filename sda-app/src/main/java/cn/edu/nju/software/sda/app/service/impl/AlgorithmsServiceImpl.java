package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.plugin.PluginManager;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlgorithmsServiceImpl implements AlgorithmsService {

    @Override
    public List<PartitionAlgorithm> getAllAvailablePartitionAlgorithm() {
        return PluginManager.getInstance().getPartitionAlgorithmList();
    }

    @Override
    public boolean isSufficientForPartitionAlgorithm(EffectiveInfo effectiveInfo, PartitionAlgorithm partitionAlgorithm) {
        return partitionAlgorithm.match(effectiveInfo);
    }

    @Override
    public PartitionAlgorithm getPartitionAlgorithmByName(String name) {
        return PluginManager.getInstance().getPlugin(PartitionAlgorithm.class, name);
    }


}
