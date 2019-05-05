package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithm;

import java.util.List;

public interface AlgorithmsService {

    List<PartitionAlgorithm> getAllAvailablePartitionAlgorithm();

    boolean isSufficientForPartitionAlgorithm(EffectiveInfo effectiveInfo, PartitionAlgorithm partitionAlgorithm);

    PartitionAlgorithm getPartitionAlgorithmByName(String name);
}
