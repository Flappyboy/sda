package cn.edu.nju.software.sda.core.entity.node;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class NodeSet <N extends Node> implements Iterable<N>{
    private Map<Type, Set<N>> nodeMap = new HashMap<>();

    public N getNodeById(String id){
        if(id == null){
            return null;
        }
        for(N node: this){
            if(id.equals(node.getId())){
                return node;
            }
        }
        return null;
    }

    public NodeSet addNode(N node){
        Type type = node.getType();
        if(type == null){
            throw new RuntimeException("缺少type  "+ node);
        }
        if(!nodeMap.containsKey(type)){
            nodeMap.put(type, new HashSet<>());
        }
        Set<N> set = nodeMap.get(type);
        if (set.contains(node)){
            set.remove(node);
        }
        set.add(node);
        return this;
    }

    public int size(){
        int s = 0;
        for (Map.Entry<Type, Set<N>> entry : nodeMap.entrySet()) {
            s += entry.getValue().size();
        }
        return s;
    }

    public NodeSet addNode(Collection<N> nodes){
        for (N node:
                nodes) {
            addNode(node);
        }
        return this;
    }

    public NodeSet addNode(NodeSet<N> nodeSet){
        for (N node :
                nodeSet) {
            addNode(node);
        }
        return this;
    }

    public Set<N> allNodes(){
        Set<N> set = new HashSet<>();
        for (N node :
                this) {
            set.add(node);
        }
        return set;
    }

    @Override
    public Iterator<N> iterator() {
        return new NodeIterator(this.nodeMap);
    }

    class NodeIterator implements Iterator<N> {
        private Iterator<Map.Entry<Type, Set<N>>> entries;

        private Iterator<N> iterator;

        public NodeIterator(Map<Type, Set<N>> nodeMap) {
            this.entries = nodeMap.entrySet().iterator();
            nextIterator();
        }

        private boolean nextIterator(){
            while(entries.hasNext()){
                Collection<N> c = entries.next().getValue();
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
        public N next() {
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

        NodeSet<?> nodeSet = (NodeSet<?>) o;

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
