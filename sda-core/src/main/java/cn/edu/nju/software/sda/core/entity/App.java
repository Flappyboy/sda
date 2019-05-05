package cn.edu.nju.software.sda.core.entity;

import cn.edu.nju.software.sda.core.entity.info.InfoSet;
import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.node.NodeSet;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import lombok.Data;

@Data
public class App <N extends Node, P extends Node> {
    private String id;

    private String name;

    /**
     * 所有节点的集合，N代表最高层节点的类型
     */
    private NodeSet<N> nodeSet;

    /**
     * 协助划分的辅助信息，如静态调用数据，动态调用数据，git数据等等
     */
    private InfoSet infoSet;

    /**
     * 当前app的划分情况，单体应用则就一个划分节点
     */
    private Partition<P> partition;
}
