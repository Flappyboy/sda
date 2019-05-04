package cn.edu.nju.software.algorithm.kmeans;

public class Test {
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

        int[] prev = new int[vexs.length];
        double[] dist = new double[vexs.length];


        // dijkstra算法获取"第4个顶点"到其它各个顶点的最短距离
        pG.dijkstra("Cc", prev, dist);

    }
}
