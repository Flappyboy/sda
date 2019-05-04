package cn.edu.nju.software.algorithm.kmeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphUtil {
    private static double INF = Double.MAX_VALUE;

//    // 邻接表中表对应的链表的顶点
//    private class ENode {
//        int ivex;       // 该边所指向的顶点的位置
//        String data;
//        int weight;     // 该边的权
//        ENode nextEdge; // 指向下一条弧的指针
//    }
//
//    // 邻接表中表的顶点
//    private class VNode {
//        int ivex;               //顶点位置
//        String data;          // 顶点信息
//        ENode firstEdge;    // 指向第一条依附该顶点的弧
//    }


    private int mEdgNum;    // 边的数量
    private int mVexNum;    //顶点数
    private VNode[] mVexs;  // 顶点数组
    private HashMap<String,VNode> nodeMap = new HashMap<String, VNode>();

    public HashMap<String, VNode> getNodeMap() {
        return nodeMap;
    }

    public int getmVexNum() {
        return mVexNum;
    }

    public VNode[] getmVexs() {
        return mVexs;
    }

    /*
     * 创建图(用已提供的矩阵)
     *
     * 参数说明：
     *     vexs  -- 顶点数组
     *     edges -- 边
     */
    public GraphUtil(String[] vexs, EData[] edges) {

        // 初始化"顶点数"和"边数"
        int vlen = vexs.length;
        int elen = edges.length;

        // 初始化"顶点"
        mVexs = new VNode[vlen];
        for (int i = 0; i < mVexs.length; i++) {
            mVexs[i] = new VNode();
            mVexs[i].data = vexs[i];
            mVexs[i].firstEdge = null;
            mVexs[i].ivex = i;
        }

        // 初始化"边"
        mEdgNum = elen;
        mVexNum = vlen;
        for (int i = 0; i < elen; i++) {
            // 读取边的起始顶点和结束顶点
            String c1 = edges[i].start;
            String c2 = edges[i].end;
            double weight = edges[i].weight;

            // 读取边的起始顶点和结束顶点
            int p1 = getPosition(c1);
            int p2 = getPosition(c2);
            // 初始化node1
            ENode node1 = new ENode();
            node1.ivex = p2;
            node1.data = c2;
            node1.weight = weight;
            // 将node1链接到"p1所在链表的末尾"
            if (mVexs[p1].firstEdge == null)
                mVexs[p1].firstEdge = node1;
            else
                linkLast(mVexs[p1].firstEdge, node1);
            // 初始化node2
            ENode node2 = new ENode();
            node2.ivex = p1;
            node2.data = c1;
            node2.weight = weight;
            // 将node2链接到"p2所在链表的末尾"
            if (mVexs[p2].firstEdge == null)
                mVexs[p2].firstEdge = node2;
            else
                linkLast(mVexs[p2].firstEdge, node2);
        }

        setDegree(mVexs);
        setNodeMap(mVexs);
    }

    //设置map
    public void setNodeMap(VNode[] mVexs){
        for (int i = 0; i < mVexs.length; i++) {
            nodeMap.put(mVexs[i].data,mVexs[i]);
        }

    }
    //设置顶点的度
    public void setDegree(VNode[] mVexs) {
        for (int i = 0; i < mVexs.length; i++) {
            VNode node = mVexs[i];
            ENode p = node.firstEdge;
            int degree = 0;
            double sumWeight = 0;
            while (p != null) {
                degree++;
                sumWeight += p.weight;
                p = p.nextEdge;
            }
            node.setDegree(degree);
            node.setSumWeight(sumWeight);
        }

    }

    //获取子图
    public GraphUtil getSubgraph(String[] nodes) {
        List<EData> edges = new ArrayList<EData>();
        for (int i = 0; i < this.mVexNum; i++) {
            VNode vNode = this.mVexs[i];
            if (isContains(nodes, vNode.data)) {
                ENode p = vNode.firstEdge;
                if (isContains(nodes, p.data)) {
                    EData eData = new EData(vNode.data, p.data, p.weight);
                    edges.add(eData);
                }
                while (p.nextEdge != null) {
                    p = p.nextEdge;
                    if (isContains(nodes, p.data)) {
                        EData eData = new EData(vNode.data, p.data, p.weight);
                        edges.add(eData);
                    }
                }
            }

        }

        EData[] edgeArr = new EData[edges.size()];
        edges.toArray(edgeArr);
        GraphUtil pG = new GraphUtil(nodes, edgeArr);
        return pG;
    }

    private boolean isContains(String[] nodes, String pointStr) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == pointStr)
                return true;
        }
        return false;
    }

    /*
     * 将node节点链接到list的最后
     */
    private void linkLast(ENode list, ENode node) {
        ENode p = list;

        while (p.nextEdge != null)
            p = p.nextEdge;
        p.nextEdge = node;
    }

    /*
     * 返回ch位置
     */
    private int getPosition(String ch) {
        for (int i = 0; i < mVexs.length; i++)
            if (mVexs[i].data == ch)
                return i;
        return -1;
    }

    /*
     * 获取边<start, end>的权值；若start和end不是连通的，则返回无穷大。
     */
    private double getWeight(int start, int end) {

        if (start == end)
            return 0;

        ENode node = mVexs[start].firstEdge;
        while (node != null) {
            if (end == node.ivex)
                return node.weight;
            node = node.nextEdge;
        }

        return INF;
    }


    /*
     * 获取图中的边
     */
    private EData[] getEdges() {
        int index = 0;
        EData[] edges;

        edges = new EData[mEdgNum];
        for (int i = 0; i < mVexs.length; i++) {

            ENode node = mVexs[i].firstEdge;
            while (node != null) {
                if (node.ivex > i) {
                    edges[index++] = new EData(mVexs[i].data, mVexs[node.ivex].data, node.weight);
                }
                node = node.nextEdge;
            }
        }

        return edges;
    }


    /*
     * Dijkstra最短路径。
     * 即，统计图中"顶点vs"到其它各个顶点的最短路径。
     *
     * 参数说明：
     *       vs -- 起始顶点(start vertex)。即计算"顶点vs"到其它顶点的最短路径。
     *     prev -- 前驱顶点数组。即，prev[i]的值是"顶点vs"到"顶点i"的最短路径所经历的全部顶点中，位于"顶点i"之前的那个顶点。
     *     dist -- 长度数组。即，dist[i]是"顶点vs"到"顶点i"的最短路径的长度。
     */
    public List<DijkstraResult> dijkstra(String vsStr, int[] prev, double[] dist) {
        List<DijkstraResult> dijkstraResults = new ArrayList<DijkstraResult>();
        // flag[i]=true表示"顶点vs"到"顶点i"的最短路径已成功获取。
        boolean[] flag = new boolean[mVexs.length];
        int vs = getPosition(vsStr);
        // 初始化
        for (int i = 0; i < mVexs.length; i++) {
            flag[i] = false;            // 顶点i的最短路径还没获取到。
            prev[i] = 0;                // 顶点i的前驱顶点为0。
            dist[i] = getWeight(vs, i); // 顶点i的最短路径为"顶点vs"到"顶点i"的权。
        }

        // 对"顶点vs"自身进行初始化
        flag[vs] = true;
        dist[vs] = 0;

        // 遍历mVexs.length-1次；每次找出一个顶点的最短路径。
        int k = 0;
        for (int i = 1; i < mVexs.length; i++) {
            // 寻找当前最小的路径；
            // 即，在未获取最短路径的顶点中，找到离vs最近的顶点(k)。
            double min = INF;
            for (int j = 0; j < mVexs.length; j++) {
                if (flag[j] == false && dist[j] < min) {
                    min = dist[j];
                    k = j;
                }
            }
            // 标记"顶点k"为已经获取到最短路径
            flag[k] = true;

            // 修正当前最短路径和前驱顶点
            // 即，当已经"顶点k的最短路径"之后，更新"未获取最短路径的顶点的最短路径和前驱顶点"。
            for (int j = 0; j < mVexs.length; j++) {
                double tmp = getWeight(k, j);
                tmp = (tmp == INF ? INF : (min + tmp)); // 防止溢出
                if (flag[j] == false && (tmp < dist[j])) {
                    dist[j] = tmp;
                    prev[j] = k;
                }
            }
        }

        // 打印dijkstra最短路径的结果
//        System.out.printf("dijkstra(" + mVexs[vs].data + "): \n");
        for (int i = 0; i < mVexs.length; i++) {
//            System.out.printf("  shortest(" + mVexs[vs].data + ", " + mVexs[i].data + ")="+dist[i]);
            DijkstraResult dijkstraResult = new DijkstraResult();
            dijkstraResult.setSourceData(mVexs[vs].data);
            dijkstraResult.setSourceId(mVexs[vs].ivex);
            dijkstraResult.setTargetData(mVexs[i].data);
            dijkstraResult.setTargetId(mVexs[i].ivex);
            dijkstraResult.setWeigth(dist[i]);
            dijkstraResults.add(dijkstraResult);
        }
        return dijkstraResults;
    }
}

