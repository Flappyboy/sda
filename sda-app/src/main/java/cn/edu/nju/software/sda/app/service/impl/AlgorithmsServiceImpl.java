package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.plugin.PluginManager;
import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlgorithmsServiceImpl implements AlgorithmsService {

    @Override
    public List<PartitionPlugin> getAllAvailablePartitionAlgorithm() {
        return PluginManager.getInstance().getPartitionPluginList();
    }

    @Override
    public boolean isSufficientForPartitionAlgorithm(EffectiveInfo effectiveInfo, PartitionPlugin partitionPlugin) {
        return partitionPlugin.match(effectiveInfo);
    }

    @Override
    public PartitionPlugin getPartitionAlgorithmByName(String name) {
        return PluginManager.getInstance().getPlugin(PartitionPlugin.class, name);
    }


}
