package cn.edu.nju.software.sda.core.domain.node;

import cn.edu.nju.software.sda.core.NodeManager;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class NodeSet implements Iterable<Node>{
    private Map<String, Set<Node>> nodeMap = new HashMap<>();

    private Integer count = 0;

    private Map<String, Node> nameNodeMap = new HashMap<>();
    private Map<String, Node> idNodeMap = new HashMap<>();

    public Map<String, Node> getIdNodeMap(){
        return idNodeMap;
    }
    public NodeSet getNodeSet(Class clazz){
        NodeSet nodes = new NodeSet();
        Set<Node> set = nodeMap.get(NodeManager.getNode(clazz));
        if(set != null) {
            for (Node node :
                    set) {
                nodes.addNode(node);
            }
        }
        return nodes;
    }

    public <T extends Node> Set<T> getSetByClass(Class<T> clazz){
        Set<Node> set = nodeMap.get(NodeManager.getNode(clazz));
        if(set == null){
            return null;
        }
        Set<T> s = new HashSet<>();
        for(Node node : set){
            s.add((T) node);
        }
        return s;
    }

    public Node getNodeById(String id){
        return idNodeMap.get(id);
    }

    public Node getNodeByName(String name){
        return nameNodeMap.get(name);
    }

    public NodeSet addNode(Node node){
        String type = NodeManager.getNode(node.getClass());
        if(type == null){
            throw new RuntimeException("This node hasn't register! "+ node);
        }
        if(!nodeMap.containsKey(type)){
            nodeMap.put(type, new HashSet<>());
        }
        Set set = nodeMap.get(type);
        if (set.contains(node)){
            count--;
            set.remove(node);
        }
        count++;
        set.add(node);
        nameNodeMap.put(node.getName(), node);
        if(node.getId()!=null) {
            idNodeMap.put(node.getId(), node);
        }
        return this;
    }

    public NodeSet addNode(Collection<Node> nodes){
        for (Node node:
                nodes) {
            addNode(node);
        }
        return this;
    }

    public NodeSet addNode(NodeSet nodeSet){
        for (Node node :
                nodeSet) {
            addNode(node);
        }
        return this;
    }

    public Set allNodes(){
        Set set = new HashSet<>();
        for (Node node :
                this) {
            set.add(node);
        }
        return set;
    }

    public Integer size(){
        return count;
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator(this.nodeMap);
    }

    class NodeIterator implements Iterator<Node> {
        private Iterator<Map.Entry<String, Set<Node>>> entries;

        private Iterator<Node> iterator;

        public NodeIterator(Map<String, Set<Node>> nodeMap) {
            this.entries = nodeMap.entrySet().iterator();
            nextIterator();
        }

        private boolean nextIterator(){
            while(entries.hasNext()){
                Collection c = entries.next().getValue();
                if(c != null){
                    iterator = c.iterator();
                    if(iterator.hasNext()){
                        return true;
                    }
                }
                entries.next();
            }
            return false;
        }

        @Override
        public boolean hasNext() {
            if (iterator == null)
                return false;

            if(iterator.hasNext())
                return true;

            return nextIterator();
        }

        @Override
        public Node next() {
            if(!hasNext()){
                return null;
            }
            return iterator.next();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        for(Node node: this){
            if(!node.equals(node)){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37);
        for(Node node: this){
            hashCodeBuilder.append(node);
        }
        return hashCodeBuilder.toHashCode();
    }
}
