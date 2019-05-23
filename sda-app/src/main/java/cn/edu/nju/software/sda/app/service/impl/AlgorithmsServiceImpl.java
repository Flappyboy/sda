package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithmManager;
import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlgorithmsServiceImpl implements AlgorithmsService {

    @Override
    public List<PartitionPlugin> getAllAvailablePartitionAlgorithm() {
        return PartitionAlgorithmManager.get();
    }

    @Override
    public PartitionPlugin getPartitionAlgorithmByName(String name) {
        return PartitionAlgorithmManager.get(name);
    }


}
