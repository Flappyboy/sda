package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithmManager;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlgorithmsServiceImpl implements AlgorithmsService {

    @Override
    public List<PartitionAlgorithm> getAllAvailablePartitionAlgorithm() {
        return PartitionAlgorithmManager.get();
    }

    @Override
    public PartitionAlgorithm getPartitionAlgorithmByName(String name) {
        return PartitionAlgorithmManager.get(name);
    }


}
