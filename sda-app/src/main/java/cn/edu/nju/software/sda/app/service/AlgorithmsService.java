package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;

import java.util.List;

public interface AlgorithmsService {

    List<PartitionPlugin> getAllAvailablePartitionAlgorithm();

    PartitionPlugin getPartitionAlgorithmByName(String name);
}
