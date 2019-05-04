package cn.edu.nju.software.algorithm.kmeans;

import java.util.*;

public class Kmeans {
    private GraphUtil graphUtil;//图
    private int k;//中心店个数
    private String[] centerPoints;//中心点
    private int iterations = 20;//最多迭代次数


    public Kmeans(GraphUtil graphUtil, int k, String[] centerPoints) {
        this.graphUtil = graphUtil;
        this.k = k;
        this.centerPoints = centerPoints;
    }

    public void printPoint(int count ,String[] centerPoints){
        System.out.println("第" + count + "次迭代，中心点是：" );
        for (int i =0 ;i<centerPoints.length;i++){
            System.out.println(centerPoints[i]);
        }
    }


    public List<GraphUtil> run() {
        int count = 1;
        printPoint(count,this.centerPoints);

        //分别计算每个点和k个起始质点之间的距离
        Map<Integer, DijkstraResult> distance = calculateDistance(this.graphUtil, this.centerPoints, this.k);
        //归类
        List<GraphUtil> graphs = classify(distance, graphUtil);
        //重新计算中心点
//        String[] newCenterPoints = calculateCenterPoints(graphs);
        String[] newCenterPoints = calculateCenterPoints(graphs, this.graphUtil);
        count++;
        printPoint(count,newCenterPoints);
        this.iterations--;
        if(!isChange(centerPoints, newCenterPoints))
            this.iterations = 0;
        while (iterations > 0) {
            //分别计算每个点和k个起始质点之间的距离
            distance = calculateDistance(this.graphUtil, newCenterPoints, this.k);
            //归类
            graphs = classify(distance, graphUtil);
            //重新计算中心点
//            String[] newCenter = calculateCenterPoints(graphs);
            String[] newCenter = calculateCenterPoints(graphs, this.graphUtil);
            this.iterations--;
            count++;
            printPoint(count,newCenter);
            if(!isChange(newCenterPoints, newCenter)) {
                this.iterations = 0;
                System.out.println("中心点一样停止迭代！");
            }
            newCenterPoints = newCenter;
        }

        return graphs;

    }

    //计算距离
    public Map<Integer, DijkstraResult> calculateDistance(GraphUtil graphUtil, String[] centerPoints, int k) {
        Map<Integer, DijkstraResult> distance = new HashMap<Integer, DijkstraResult>();
        for (int i = 0; i < k; i++) {
            int[] prev = new int[graphUtil.getmVexNum()];
            double[] dist = new double[graphUtil.getmVexNum()];
            List<DijkstraResult> dijkstraResults = graphUtil.dijkstra(centerPoints[i], prev, dist);
            for (DijkstraResult dijkstraResult : dijkstraResults) {
                if (!isContains(centerPoints, dijkstraResult.getTargetData())) {
                    int key = dijkstraResult.getTargetId();
                    if (!distance.containsKey(key)) {
                        distance.put(key, dijkstraResult);
                    } else {
                        DijkstraResult dijkstraResultOld = distance.get(key);
                        //
                        if (dijkstraResultOld.getWeigth() > dijkstraResult.getWeigth())
                            distance.put(key, dijkstraResult);
                    }
                }
            }
        }
        return distance;
    }

    //归类
    public List<GraphUtil> classify(Map<Integer, DijkstraResult> distance, GraphUtil graphUtil) {
        List<GraphUtil> graphCategory = new ArrayList<GraphUtil>();
        Map<Integer, List<DijkstraResult>> nodes = new HashMap<Integer, List<DijkstraResult>>();
        for (Map.Entry<Integer, DijkstraResult> entry : distance.entrySet()) {
            int key = entry.getValue().getSourceId();
            DijkstraResult dijkstraResult = entry.getValue();
            List<DijkstraResult> dijkstraResults = new ArrayList<DijkstraResult>();
            if (nodes.containsKey(key)) {
                dijkstraResults = nodes.get(key);
                dijkstraResults.add(dijkstraResult);
//                nodes.put(key,dijkstraResults);
            } else {
                dijkstraResults.add(dijkstraResult);
            }
            nodes.put(key, dijkstraResults);
        }
        graphCategory = toGraphs(nodes, graphUtil);

        return graphCategory;
    }


    //划分成子图
    public List<GraphUtil> toGraphs(Map<Integer, List<DijkstraResult>> nodes, GraphUtil graphUtil) {
        List<GraphUtil> graphUtils = new ArrayList<GraphUtil>();
        for (Map.Entry<Integer, List<DijkstraResult>> entry : nodes.entrySet()) {
            List<DijkstraResult> dijkstraResults = entry.getValue();
            Set<String> vNode = new HashSet<String>();
            for (DijkstraResult dijkstraResult : dijkstraResults) {
                vNode.add(dijkstraResult.getSourceData());
                vNode.add(dijkstraResult.getTargetData());
            }
            String[] vNodeArr = new String[vNode.size()];
            vNode.toArray(vNodeArr);
            GraphUtil subgraph = graphUtil.getSubgraph(vNodeArr);
            graphUtils.add(subgraph);
        }
        return graphUtils;
    }


    //重新计算中心点
    public String[] calculateCenterPoints(List<GraphUtil> graphs, GraphUtil graphUtil) {
        HashMap<String, VNode> nodeMap = graphUtil.getNodeMap();
        List<String> centerPoints = new ArrayList<String>();
        for (GraphUtil graph : graphs) {
            VNode[] vNodes = graph.getmVexs();
            String centerPoint = "";
            int centerDegree = 0;
//            double weight = 0;
            double weight = 0;
            for (int i = 0; i < vNodes.length; i++) {
                String pointData = vNodes[i].data;
                VNode node = nodeMap.get(pointData);
                int degree = node.degree;
//                double sumWeight = p.weight;
                double sumWeight = node.sumWeight;

                if (degree > centerDegree) {
                    centerDegree = degree;
                    centerPoint = pointData;
                    weight = sumWeight;
                } else if (degree == centerDegree) {
                    //
                    if (sumWeight < weight) {
                        centerDegree = degree;
                        centerPoint = pointData;
                        weight = sumWeight;
                    }
                }
            }
            centerPoints.add(centerPoint);

        }
        String[] cpArr = new String[centerPoints.size()];
        centerPoints.toArray(cpArr);
        return cpArr;
    }

    //重新计算中心点
    public String[] calculateCenterPoints(List<GraphUtil> graphs) {
        List<String> centerPoints = new ArrayList<String>();
        for (GraphUtil graph : graphs) {
            VNode[] vNodes = graph.getmVexs();
            String centerPoint = "";
            int centerDegree = 0;
//            double weight = 0;
            int weight = 0;
            for (int i = 0; i < vNodes.length; i++) {
                String pointData = vNodes[i].data;
                ENode p = vNodes[i].firstEdge;
                int degree = 1;
//                double sumWeight = p.weight;
                int sumWeight = 0;
                while (p.nextEdge != null) {
                    degree++;
                    sumWeight += p.weight;
                    p = p.nextEdge;
                }

                if (degree > centerDegree) {
                    centerDegree = degree;
                    centerPoint = pointData;
                    weight = sumWeight;
                } else if (degree == centerDegree) {
                    //
                    if (sumWeight < weight) {
                        centerDegree = degree;
                        centerPoint = pointData;
                        weight = sumWeight;
                    }
                }
            }
            centerPoints.add(centerPoint);
        }
        String[] cpArr = new String[centerPoints.size()];
        centerPoints.toArray(cpArr);
        return cpArr;
    }

    //判断中心是否不变了,不变停止迭代
    public boolean isChange(String[] oldCenter, String[] newCenter) {
        for (int i = 0; i < newCenter.length; i++) {
            if (!isContains(oldCenter, newCenter[i]))
                return true;//改变了
        }
        return false;//完全一样
    }

    //数组中是否包含某个值
    public boolean isContains(String[] centerPoints, String pointStr) {
        for (int i = 0; i < centerPoints.length; i++) {
            if (centerPoints[i] == pointStr)
                return true;
        }
        return false;
    }

}
