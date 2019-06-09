package cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util;

import cn.edu.nju.software.sda.plugin.function.info.impl.gitcommit.entity.GitCommitFileEdge;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class MST {

    public static List<GitCommitFileEdge> getEdges(Map<String, GitCommitFileEdge> gitCommitFileEdgeMap){
        List<GitCommitFileEdge> edges = new ArrayList<GitCommitFileEdge>();
        for (Map.Entry<String, GitCommitFileEdge> entry : gitCommitFileEdgeMap.entrySet()) {
//            GitCommitFileEdge temp = entry.getValue();
//            int i=0;
//            for(i=0;i<edges.size();i++){
//                GitCommitFileEdge tmp = edges.get(i);
//                if((temp.getSourceName().equals(tmp.getSourceName())&&temp.getTargetName().equals(tmp.getSourceName()))||(temp.getSourceName().equals(tmp.getTargetName())&&temp.getTargetName().equals(tmp.getSourceName()))){
//                    edges.get(i).setCount(tmp.getCount()+1);
//                    break;
//                }
//            }
//            if(i==edges.size()){
            edges.add(entry.getValue());
//            }
        }
        return edges;
    }

    private static SimpleWeightedGraph createGraph(List<GitCommitFileEdge> edges){
        SimpleWeightedGraph<String, WeightedEdge> graph = new SimpleWeightedGraph<>(WeightedEdge.class);
        for(int i=0;i<edges.size();i++){
            GitCommitFileEdge edge = edges.get(i);
            if(edge.getSourceName().equals(edge.getTargetName()))
                continue;
            System.out.println("信息： "+edge.getSourceName()+"  "+edge.getTargetName());
            graph.addVertex(edge.getSourceName());
            graph.addVertex(edge.getTargetName());

            WeightedEdge currentEdge = new WeightedEdge();
            currentEdge.setScore(1.0/edge.getCount());
//            System.out.println(edge.getSourceName()+"  "+edge.getTargetName()+"  "+edge.getCount());
            graph.addEdge(edge.getSourceName(), edge.getTargetName(),currentEdge);

            graph.setEdgeWeight(currentEdge, (1.0/edge.getCount()));
        }
        return graph;
    }

    public static SpanningTreeAlgorithm.SpanningTree<WeightedEdge> calcMST(List<GitCommitFileEdge> edges){
        KruskalMinimumSpanningTree<String, WeightedEdge> mst = new KruskalMinimumSpanningTree<>(createGraph(edges));
        return mst.getSpanningTree();
    }

}

