package cn.edu.nju.software.sda.app.service;


import cn.edu.nju.software.sda.app.entity.*;

import java.util.List;

public interface PartitionResultEdgeService {

    /**
     * 统计划分中，各划分节点之间的边，并存入数据库，以便查询
     * @param partitionInfo
     */
    void statisticsPartitionResultEdge(PartitionInfo partitionInfo);

    /**
     * 删除划分中所统计的边的数据
     * @param partitionInfo
     */
    void clearPartitionResultEdge(PartitionInfo partitionInfo);

    List<PartitionResultEdge> findPartitionResultEdge(String partitionId);

    List<PartitionResultEdgeCall> findPartitionResultEdgeCallByEdgeId(String edgeId, Integer page, Integer pageSize);
    int countOfPartitionResultEdgeCallByEdgeId(String edgeId);

    /**
     * 找到在某个划分中和某个类相关的边
     * @param partitionResultId
     * @param nodeId
     * @return
     */
    List<PartitionResultEdge> findPartitionResultEdgeByNode(String partitionResultId, String nodeId);

    /**
     * 找到和某个划分相关的边
     * @param partitionId
     * @param partitionResultId
     * @return
     */
    List<PartitionResultEdge> findPartitionResultEdgeByPartitionResult(String partitionId, String partitionResultId);

    /**
     * 找到当前划分中，调用信息对应的划分结果间的调用
     * @param pairRelationEntityList
     * @param partitionId
     * @return
     */
    List<PartitionResultEdge> findPartitionResultEdge(List<PairRelationEntity> pairRelationEntityList, String partitionId);

    /**
     * 向partitionResultEdge中填入对应的Call
     * @param partitionResultEdge
     */
    void fillPartitionResultEdgeCall(PartitionResultEdge partitionResultEdge);
    void fillPartitionResultEdgeCall(List<PartitionResultEdge> partitionResultEdge);

    void removeEdgeCall(String edgeCallId);
}
