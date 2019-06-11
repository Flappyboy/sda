#  Partitioning Algorithm

## Louvain

**Introduction:** Louvain algorithm is a community discovery algorithm based on module degree, and its optimization goal is to maximize the module degree of the whole community network. 

**Parameter:** 
    
    Weighted Directed Graph
    You need to select at least one static call data or one dynamic call data.

## MST

**Introduction:** By constructing the minimum spanning tree and using the depth traversal algorithm to cluster the association graph, MST obtains the candidate set of micro services.

**Parameter:**

    SplitThreshold: expected class threshold for microservices
    NumServices: expected number of microservices
    Weighted Directed Graph
    You need to select at least one static call data or one dynamic call data and you can select a git data.
