package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;

import java.util.List;

public interface AlgorithmsService {

    List<PartitionAlgorithm> getAllAvailablePartitionAlgorithm();

    PartitionAlgorithm getPartitionAlgorithmByName(String name);
}
