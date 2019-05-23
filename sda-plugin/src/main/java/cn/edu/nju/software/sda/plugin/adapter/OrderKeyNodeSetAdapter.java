package cn.edu.nju.software.sda.plugin.adapter;

import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderKeyNodeSetAdapter extends NodeSet {

    private Long interval;

    private Long startValue;

    private NodeSet nodeSet;

    private List<Node> keyList;

    private Map<Node, Integer> keyMap;

    public OrderKeyNodeSetAdapter(NodeSet nodeSet) {
        this(nodeSet,0l);
    }

    public OrderKeyNodeSetAdapter(NodeSet nodeSet, Long startValue) {
        this(nodeSet, startValue, 1l);
    }

    public OrderKeyNodeSetAdapter(NodeSet nodeSet, Long startValue, Long interval) {
        this.interval = interval;
        this.startValue = startValue;
        this.nodeSet = nodeSet;
        this.addNode(nodeSet);
        generateKey();
    }

    private void generateKey() {
        keyList = new ArrayList<>(this.allNodes());
        keyMap = new HashMap<>();
        for(int i=0; i<keyList.size(); i++){
            keyMap.put(keyList.get(i), i);
        }
    }

    public Node getNode(Long key){
        Integer index = getIndex(key);
        return keyList.get(index);
    }

    public int getIndex(Long key){
        return Math.toIntExact((key - startValue) / interval);
    }

    public long getKey(int index){
        return startValue + interval * index;
    }

    public long getKey(Node node){
        return getKey(keyMap.get(node));
    }
}
