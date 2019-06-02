package cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString

public class Component {
    private List<ClassNode> nodes;
    private boolean visited;

    public Component(){
        nodes = new ArrayList<>();
        visited = false;
    }

    public void addNode(ClassNode node){
        nodes.add(node);
    }

    public int getSize() {
        return this.nodes.size();
    }

    public List<String> getFilePaths(){
        return this.nodes.stream().map(classNode -> classNode.getClassName()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Component)) return false;

        Component component = (Component) o;

        return nodes.equals(component.nodes);

    }

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }

}
