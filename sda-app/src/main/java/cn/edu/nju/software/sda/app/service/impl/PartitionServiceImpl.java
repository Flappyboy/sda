package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.info.Info;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
public class PartitionServiceImpl implements PartitionService {
    @Autowired
    private PartitionInfoMapper partitionMapper;

    @Autowired
    private PairRelationInfoService pairRelationInfoService;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private PartitionPairMapper partitionPairMapper;

    @Autowired
    private PartitionNodeService partitionNodeService;

    @Autowired
    private AppService appService;

    @Override
    public PartitionInfoEntity findPartitionById(String partitionId) {
        PartitionInfoEntity partitionInfoEntity = partitionMapper.selectByPrimaryKey(partitionId);
        Example example = new Example(PartitionPairEntity.class);
        PartitionPairEntity partitionPairEntity = new PartitionPairEntity();
        partitionPairEntity.setPartitionInfoId(partitionId);
        example.createCriteria().andEqualTo(partitionPairEntity);
        List<PartitionPairEntity> partitionPairEntities = partitionPairMapper.selectByExample(example);
        List<PairRelationInfoEntity> pairRelationInfoEntities = new ArrayList<>();
        for (PartitionPairEntity ppe :
                partitionPairEntities) {
            PairRelationInfoEntity prie = pairRelationInfoService.queryInfoById(ppe.getPairRelationInfoId());
            if(prie != null){
                pairRelationInfoEntities.add(prie);
            }
        }
        partitionInfoEntity.setPairRelationInfoEntityList(pairRelationInfoEntities);
        return partitionInfoEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionInfoEntity savePartition(PartitionInfoEntity partition) {
        String id = Sid.nextShort();
        partition.setId(id);
        partition.setCreatedAt(new Date());
        partition.setUpdatedAt(new Date());
        partition.setFlag(1);
        partition.setStatus(Info.InfoStatus.SAVING.name());
        partitionMapper.insertSelective(partition);
        return partition;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delPartition(String id) {
        PartitionInfoEntity partition = new PartitionInfoEntity();
        partition.setId(id);
        partition.setFlag(0);
        updatePartition(partition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartition(PartitionInfoEntity partition) {
        partition.setUpdatedAt(new Date());
        partitionMapper.updateByPrimaryKeySelective(partition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PageQueryDto<PartitionInfoEntity> queryPartitionInfoPaged(@NotNull PageQueryDto<PartitionInfoEntity> dto, PartitionInfoEntity partitionInfoEntity) {
        // 开始分页
        Page p = PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), true);

        Example example = new Example(PartitionInfoEntity.class);
        partitionInfoEntity.setFlag(1);
        example.createCriteria().andEqualTo(partitionInfoEntity);
        example.setOrderByClause("created_at desc");
        List<PartitionInfoEntity> partitionList = partitionMapper.selectByExample(example);
        /*for (PartitionInfoEntity pife :
                partitionList) {
            pife.setAppName(appService.queryAppById(pife.getAppId()).getName());
        }*/
        return dto.addResult(partitionList, p.getTotal());
    }

/*

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraph getGraph(String partitionInfoId) {
        PartitionGraph partitionGraph = new PartitionGraph();

        //找出所有社区
        PartitionInfoEntity partitionInfo = partitionMapper.selectByPrimaryKey(partitionInfoId);
        if(partitionInfo == null)
            return  null;
        int type = partitionInfo.getType();
        String parentId = partitionInfo.getParentId();
        String namicanalysisinfoid = partitionInfo.getDynamicanalysisinfoid();


        Example example = new Example(PartitionNodeEntity.class);
        PartitionNodeEntity partitionResultExample = new PartitionNodeEntity();
        partitionResultExample.setPartitionId(partitionInfoId);
        partitionResultExample.setFlag(1);
        example.createCriteria().andEqualTo(partitionResultExample);
        List<PartitionNodeEntity> partitionResultList = partitionResultMapper.selectByExample(example);

        HashMap<String, List<String>> nodes = new HashMap<>();

        //社区节点
        List<PartitionGraphNode> partitionNodes = new ArrayList<>();
        for (PartitionNodeEntity partitionResult : partitionResultList) {
            List<String> nodeIds = new ArrayList<>();
            PartitionGraphNode partitionNode = new PartitionGraphNode();
            partitionNode.setCommunity(partitionResult);
            List<NodeEntity> nodeEntities = new ArrayList<>();

            PartitionDetailEntity pd = new PartitionDetailEntity();
            pd.setPartitionNodeId(partitionResult.getId());
            pd.setFlag(1);
            Example pdExample = new Example(PartitionDetailEntity.class);
            example.createCriteria().andEqualTo(pd);

            List<PartitionDetailEntity> partitionDetails = partitionDetailMapper.selectByExample(pdExample);
            for (PartitionDetailEntity partitionDetail : partitionDetails) {
                if (type == 0) {
                    NodeEntity nodeEntity = nodeMapper.selectByPrimaryKey(partitionDetail.getNodeId());
                    nodeEntities.add(nodeEntity);
                    nodeIds.add(nodeEntity.getId());
                }
                if (type == 1) {
                    MethodNode methodNode = methodNodeMapper.selectByPrimaryKey(partitionDetail.getNodeId());
                    methodNodes.add(methodNode);
                    nodeIds.add(methodNode.getId());
                }
            }
            nodes.put(partitionResult.getId(), nodeIds);
            partitionNode.setNodeEntities(nodeEntities);
            partitionNode.setMethodNodes(methodNodes);
            partitionNode.setClaaSize(nodeEntities.size());
            partitionNode.setMethodSize(methodNodes.size());
            partitionNodes.add(partitionNode);
        }

        //社区边
        List<PartitionGraphEdge> partitionGraphEdges = new ArrayList<>();
        for (PartitionGraphNode partitionNode1 : partitionNodes) {
            for (PartitionGraphNode partitionNode2 : partitionNodes) {
                String communityId1 = partitionNode1.getCommunity().getId();
                String communityId2 = partitionNode2.getCommunity().getId();
                if (communityId1 != communityId2) {
                    //查询静态的边
                    List<StaticCallInfo> staticCallInfos = new ArrayList<>();
                    staticCallInfos = getStaticEdge(nodes.get(communityId1),nodes.get(communityId2),type,parentId);
                    staticCallInfos.addAll(getStaticEdge(nodes.get(communityId2),nodes.get(communityId1),type,parentId));
                    //查询动态的边
                    List<DynamicCallInfo> dynamicCallInfos = new ArrayList<>();
                    dynamicCallInfos = getDynamicEdge(nodes.get(communityId1),nodes.get(communityId2),type,namicanalysisinfoid);
                    dynamicCallInfos.addAll(getDynamicEdge(nodes.get(communityId2),nodes.get(communityId1),type,namicanalysisinfoid));
                    //合并边
                    List<PartitionNodeEdge>  partitionNodeEdges = getMergedEdge(staticCallInfos,dynamicCallInfos,type);
                    //判断两社区之间有边
                    if(partitionNodeEdges!=null&&partitionNodeEdges.size()>0){
                        PartitionGraphEdge partitionGraphEdge = new PartitionGraphEdge();
                        partitionGraphEdge.setSourceCommunityId(communityId1);
                        partitionGraphEdge.setTargetCommunityId(communityId2);
                        partitionGraphEdge.setEdges(partitionNodeEdges);
                        partitionGraphEdges.add(partitionGraphEdge);
                    }
                }
            }
        }

        partitionGraph.setPartitionNodes(partitionNodes);
        partitionGraph.setPartitionEdges(partitionGraphEdges);
        return partitionGraph;
    }
*/

}
