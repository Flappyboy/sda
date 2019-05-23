package cn.edu.nju.software.sda.core.domain;

import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class App{

    private String id;

    private String name;

    /**
     * 所有节点的集合，N代表最高层节点的类型
     */
    private NodeSet nodeSet;

    /**
     * 协助划分的辅助信息，如静态调用数据，动态调用数据，git数据等等
     */
    private InfoSet infoSet;

    /**
     * 当前app的划分情况，单体应用则就一个划分节点
     */
    private Partition partition;
}
