package cn.edu.nju.software.algorithm.kmeans;

import java.util.List;

public class KmeansMain {
    public static void main(String[] args) {
        String[] vexs = {"Aa", "Bb", "Cc", "Dd", "Ee", "Ff", "Gg"};
        EData[] edges = {
                // 起点 终点 权
                new EData("Aa", "Bb", 12),
                new EData("Aa", "Ff", 16),
                new EData("Aa", "Gg", 14),
                new EData("Bb", "Cc", 10),
                new EData("Bb", "Ff", 7),
                new EData("Cc", "Dd", 3),
                new EData("Cc", "Ee", 5),
                new EData("Cc", "Ff", 6),
                new EData("Dd", "Ee", 4),
                new EData("Ee", "Ff", 2),
                new EData("Ee", "Gg", 8),
                new EData("Ff", "Gg", 9),
        };
        GraphUtil pG;

        // 采用已有的"图"
        pG = new GraphUtil(vexs, edges);
        String[] point = {"Aa", "Bb"};

        Kmeans kmeans = new Kmeans(pG,2,point);
        List<GraphUtil> graphs = kmeans.run();
        System.out.println("");
        System.out.println("打印结果：");
        int i = 1;
        for(GraphUtil graphUtil:graphs){
            System.out.println("第"+i+"类：");
            printGraph(graphUtil);
        }

    }

    public static void printGraph(GraphUtil graphUtil){

        VNode[] nodes = graphUtil.getmVexs();
        for(int i=0;i<nodes.length;i++) {
            System.out.println(nodes[i].data);

        }

    }

}
