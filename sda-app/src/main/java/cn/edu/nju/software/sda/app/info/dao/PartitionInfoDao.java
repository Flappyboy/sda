package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.info.PartitionInfo;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Component
public class PartitionInfoDao implements InfoDao<PartitionInfo> {

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private PartitionNodeService partitionNodeService;

    @Autowired
    private PartitionNodeMapper partitionNodeMapper;

    @Autowired
    private PartitionInfoMapper partitionInfoMapper;

    @Autowired
    private PartitionDetailMapper partitionDetailMapper;

    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private NodeInfoDao nodeInfoDao;

    @Override
    public PartitionInfo saveProfile(PartitionInfo info) {
        partitionInfoMapper.insert(PartitionInfoEntity.create(info));
        return info;
    }

    @Override
    public PartitionInfo saveDetail(PartitionInfo info) {
        Partition partition = info.getPartition();
        Set<PartitionNode> partitionSet = partition.getPartitionNodeSet();
        Map<String, NodeEntity> nodeEntityMap = nodeService.findNameMapByAppid(info.getParentId());

        List<PartitionDetailEntity> partitionDetailEntities = new ArrayList<>();
        for (PartitionNode pn :
                partitionSet) {
            pn.setId(Sid.nextShort());
            partitionNodeMapper.insert(PartitionNodeEntity.create(info.getId(), pn));
            for (Node node :
                    pn.getNodeSet()) {
                NodeEntity nodeEntity = nodeEntityMap.get(node.getName());
                if(nodeEntity == null){
                    System.err.println("没有node: "+node.getName());
                    continue;
                }
                node.setId(nodeEntityMap.get(node.getName()).getId());
                PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
                partitionDetailEntity.setId(Sid.nextShort());
                partitionDetailEntity.setNodeId(node.getId());
                partitionDetailEntity.setFlag(1);
                partitionDetailEntity.setPartitionNodeId(pn.getId());
                partitionDetailEntity.setCreatedAt(new Date());
                partitionDetailEntity.setUpdatedAt(new Date());
                partitionDetailEntities.add(partitionDetailEntity);
            }
        }
        partitionDetailMapper.insertList(partitionDetailEntities);

        PartitionInfoEntity partitionInfoEntity = new PartitionInfoEntity();
        partitionInfoEntity.setId(info.getId());
        partitionInfoEntity.setStatus(Info.InfoStatus.COMPLETE.name());
        partitionInfoMapper.updateByPrimaryKeySelective(partitionInfoEntity);
        return info;
    }

    @Override
    public PartitionInfo updateProfileInfoById(PartitionInfo info) {
        return null;
    }

    @Override
    public PartitionInfo deleteById(String infoId) {
        partitionService.delPartition(infoId);
        return null;
    }

    @Override
    public PartitionInfo queryProfileInfoById(String infoId) {
        return null;
    }

    @Override
    public List<PartitionInfo> queryProfileInfoByAppId(String appId) {
        return null;
    }

    @Override
    public List<PartitionInfo> queryProfileInfoByAppIdAndInfoName(String appId, String infoName) {
        Example example = new Example(PartitionInfoEntity.class);
        PartitionInfoEntity partitionInfoEntity = new PartitionInfoEntity();
        partitionInfoEntity.setFlag(1);
        example.createCriteria().andEqualTo(partitionInfoEntity);
        example.setOrderByClause("created_at desc");
        List<PartitionInfoEntity> partitionList = partitionInfoMapper.selectByExample(example);
        return PartitionInfoEntity.toPartitionInfoList(partitionList);
    }

    @Override
    public PartitionInfo queryDetailInfoById(String infoId) {
        PartitionInfoEntity partitionInfoEntity = partitionInfoMapper.selectByPrimaryKey(infoId);
        List<PartitionNodeEntity> partitionNodeEntities = partitionNodeService.queryPartitionResult(infoId);
        NodeSet nodeSet = nodeInfoDao.queryDetailInfoById(partitionInfoEntity.getAppId()).getNodeSet();


        Partition partition = new Partition();
        partition.setId(partitionInfoEntity.getId());
        partition.setName(partitionInfoEntity.getDesc());

        Set<PartitionNode> partitionNodes = new HashSet<>();
        for (PartitionNodeEntity pne :
                partitionNodeEntities) {
            PartitionNode partitionNode = new PartitionNode(pne.getName());
            partitionNode.setId(pne.getId());
            NodeSet ns = new NodeSet();
            List<PartitionDetailEntity> pdeList = partitionDetailService.queryPartitionDetailListByPartitionNodeId(pne.getId());
            for (PartitionDetailEntity pde:
                    pdeList) {
                Node node = nodeSet.getNodeById(pde.getNodeId());
                if(node!=null){
                    ns.addNode(node);
                }
            }
            partitionNode.setNodeSet(ns);
            partitionNodes.add(partitionNode);
        }
        partition.setPartitionNodeSet(partitionNodes);
        PartitionInfo partitionInfo = new PartitionInfo(partition);
        return partitionInfo;
    }
}
