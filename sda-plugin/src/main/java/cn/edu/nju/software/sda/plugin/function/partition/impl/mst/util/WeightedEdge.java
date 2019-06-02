package cn.edu.nju.software.sda.plugin.function.partition.impl.mst.util;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightedEdge extends DefaultWeightedEdge{
    private static final long serialVersionUID = 708706467350994234L;

    private double score = 1.0D;

    public double getScore() {
        return this.score;
    }

    public void setScore(double score){
        this.score = score;
    }

    public String getFirstFileName(){
        return (String) this.getSource();
    }

    public String getSecondFileName(){
        return (String) this.getTarget();
    }

    @Override
    public String toString() {
        return "WeightedEdge{" +
                "score=" + score + "  nodes:" + this.getSource() + " : " + this.getTarget()+ "}";
    }
}
