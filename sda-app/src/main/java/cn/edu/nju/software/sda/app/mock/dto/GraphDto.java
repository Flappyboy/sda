package cn.edu.nju.software.sda.app.mock.dto;

import java.util.ArrayList;
import java.util.List;

public class GraphDto {
    private String type = "force";
    private List<NodeDto> nodes = new ArrayList<>();
    private List<EdgeDto> links = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NodeDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDto> nodes) {
        this.nodes = nodes;
    }

    public List<EdgeDto> getLinks() {
        return links;
    }

    public void setLinks(List<EdgeDto> links) {
        this.links = links;
    }

    public void addNode(NodeDto nodeDto){
        nodes.add(nodeDto);
    }

    public void addEdge(EdgeDto edgeDto){
        links.add(edgeDto);
    }
}
