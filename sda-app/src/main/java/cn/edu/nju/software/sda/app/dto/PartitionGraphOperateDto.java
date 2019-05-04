package cn.edu.nju.software.sda.app.dto;

import cn.edu.nju.software.sda.app.mock.dto.EdgeDto;
import cn.edu.nju.software.sda.app.mock.dto.NodeDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class PartitionGraphOperateDto {
    private Boolean reload = false;

    private List<NodeDto> deleteNodes = new ArrayList<>();

    private List<NodeDto> addNodes = new ArrayList<>();

    private Map<String, NodeDto> putNodes = new HashMap<>();

    private List<EdgeDto> deleteEdges = new ArrayList<>();

    private List<EdgeDto> addEdges = new ArrayList<>();

    private Map<String, EdgeDto> putEdges = new HashMap<>();

    public void putNode(NodeDto nodeDto){
        NodeDto node = putNodes.get(nodeDto.getId());
        if(node == null){
            node = nodeDto;
            putNodes.put(node.getId(), node);
        }else{
            node.setSize(node.getSize()+nodeDto.getSize());
        }
    }
    public void putEdge(EdgeDto edgeDto){
        EdgeDto edge = putEdges.get(edgeDto.getId());
        if(edge == null){
            edge = edgeDto;
            putEdges.put(edge.getId(), edge);
        }else{
            edge.setCount(edge.getCount()+edge.getCount());
        }
    }

    public static class Builder{
        PartitionGraphOperateDto obj = new PartitionGraphOperateDto();
        public void graphReload(){
            obj.setReload(true);
        }
        public void deleteNode(NodeDto nodeDto){

        }
        public void addNode(NodeDto nodeDto){

        }
        public void putNode(NodeDto nodeDto){

        }
        public void deleteEdge(EdgeDto edgeDto){

        }
        public void addEdge(EdgeDto edgeDto){

        }
        public void putEdge(EdgeDto edgeDto){

        }
        public void attachOperate(PartitionGraphOperateDto attachedPgod){

        }
        public PartitionGraphOperateDto build(){
            return obj;
        }
    }

    public void refresh(){

    }
}
