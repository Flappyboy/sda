#  Partitioning Algorithm

## Louvain

**Introduction:** Louvain algorithm is a community discovery algorithm based on module degree, and its optimization goal is to maximize the module degree of the whole community network. The Louvain algorithm consists of two phases. In the first step, it continuously traverses the nodes in the network and tries to add single nodes to the community that can achieve the maximum modularity improvement, until all the nodes are no longer changed. In step 2, it processes the results of the first stage, and reconstructs the network by merging small communities into a supernode. At this time, the edge weight is the sum of the edge weight of all the original nodes in the two nodes. Iterate these two steps until the algorithm is stable.

**Step：**

  1). Each node is regarded as a community;
  
  2). In turn, try to assign each node to its neighbor's community and calculate ΔQ. Find the largest ΔQ. If maxΔQ>0, assign the node to the corresponding community. Otherwise it will remain unchanged;
  
  3). Repeat 2) until the community of all nodes does not change;
  
  4). Compressing all the nodes in the same community into a new node, transforming the weight of the edges between the nodes in the community into the weight of the ring of the new node, and the weight of the edges between the communities into the weight of the edges between the new nodes;
  
  5). Repeat 1) until the modularity of the whole graph does not change;

**Parameter:** 
    
    Weighted Directed Graph
    You need to select at least one static call data or one dynamic call data.

## MST

**Introduction:** By constructing the minimum spanning tree and using the depth traversal algorithm to cluster the association graph, MST obtains the candidate set of micro services.

**Step:**

1). Build the minimum spanning tree

2). Remove the most expensive edge

3). Depth traversal clustering

4). If the number of partitions is smaller than the expected number of micro-services, step 2 is performed for each current partition; otherwise, step 5 is performed

5). Determine whether each partition meets the expected micro service class threshold. If it does, stop. If it does not, return to step 2

**Parameter:**

    SplitThreshold: expected class threshold for microservices
    NumServices: expected number of microservices
    Weighted Directed Graph
    You need to select at least one static call data or one dynamic call data and you can select a git data.
