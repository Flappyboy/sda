package cn.edu.nju.software.sda.plugin.service.partition;

import cn.edu.nju.software.sda.core.entity.App;
import cn.edu.nju.software.sda.core.entity.EffectiveInfo;
import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import cn.edu.nju.software.sda.plugin.service.base.BaseService;

import java.io.File;

public interface PartitionAlgorithm extends BaseService {

    boolean match(EffectiveInfo effectiveInfo);

//    <P extends Node> Partition<N> partition(App<N, P> app);
    <N extends Node, P extends Node>Partition<N> partition(App<N, P> app, File workspace);
}
