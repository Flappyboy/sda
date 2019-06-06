package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.PartitionNodeEdgePairMapper;
import cn.edu.nju.software.sda.app.dao.PartitionNodeEdgeMapper;
import cn.edu.nju.software.sda.app.dao.PartitionPairMapper;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Slf4j
@Service
public class PartitionNodeEdgeServiceImpl implements PartitionNodeEdgeService {

    @Autowired
    private PartitionNodeEdgeMapper partitionNodeEdgeMapper;

    @Autowired
    private PartitionNodeEdgePairMapper partitionNodeEdgePairMapper;

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private PartitionNodeService partitionNodeService;

    @Autowired
    private PairRelationService pairRelationService;

    @Autowired
    private PartitionPairMapper partitionPairMapper;

    @Override
    public void resetPartitionPair(String partitionInfoId, List<String> pairRelationInfoId) {
        Example example = new Example(PartitionPairEntity.class);
        PartitionPairEntity partitionPairEntity = new PartitionPairEntity();
        partitionPairEntity.setPartitionInfoId(partitionInfoId);
        example.createCriteria().andEqualTo(partitionPairEntity);
        List<PartitionPairEntity> partitionPairEntities = partitionPairMapper.selectByExample(example);
        for (PartitionPairEntity ppe :
                partitionPairEntities) {
            partitionPairMapper.deleteByPrimaryKey(ppe.getId());
        }
        for (String pairInfoId :
                pairRelationInfoId) {
            partitionPairEntity = new PartitionPairEntity();
            partitionPairEntity.setId(Sid.nextShort());
            partitionPairEntity.setPartitionInfoId(partitionInfoId);
            partitionPairEntity.setPairRelationInfoId(pairInfoId);
            partitionPairMapper.insert(partitionPairEntity);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void statisticsPartitionResultEdge(String partitionInfoId) {
        PartitionInfoEntity partitionInfoEntity = partitionService.findPartitionById(partitionInfoId);
        clearPartitionNodeEdge(partitionInfoEntity);

        List<PairRelationInfoEntity> pairRelationInfoEntities = partitionInfoEntity.getPairRelationInfoEntityList();

        for (PairRelationInfoEntity pair :
                pairRelationInfoEntities) {
            List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityFromStaticList = partitionNodeEdgeMapper.statisticsEdges(partitionInfoEntity.getId(), pair.getId());
            for (PartitionNodeEdgeEntity p: partitionNodeEdgeEntityFromStaticList) {
                if(p.getSourceId() == null || p.getTargetId() == null)
                    continue;
                if(p.getSourceId().equals(p.getTargetId()))
                    continue;
                p.setId(Sid.nextShort());
                partitionNodeEdgeMapper.insert(p);
                List<PairRelationEntity> pairRelationEntities = p.getPairRelationList();
                List<PartitionNodeEdgePairEntity> partitionNodeEdgePairEntities = new ArrayList<>();
                for(PairRelationEntity pairRelationEntity: pairRelationEntities){
                    PartitionNodeEdgePairEntity partitionNodeEdgePairEntity = new PartitionNodeEdgePairEntity();
                    partitionNodeEdgePairEntity.setId(Sid.nextShort());
                    partitionNodeEdgePairEntity.setCallId(pairRelationEntity.getId());
                    partitionNodeEdgePairEntity.setEdgeId(p.getId());
                    partitionNodeEdgePairEntities.add(partitionNodeEdgePairEntity);
                }
                partitionNodeEdgePairMapper.insertList(partitionNodeEdgePairEntities);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void clearPartitionNodeEdge(PartitionInfoEntity partitionInfoEntity) {
        // TODO
        // 级联删除edgePair

        //删除edges
        List<String> idList = partitionNodeService.findPartitionResultIds(partitionInfoEntity.getId());
        if(idList.size() == 0)
            return;
        Example example = new Example(PartitionNodeEdgeEntity.class);
        example.createCriteria().andIn("sourceId",idList);
        partitionNodeEdgeMapper.deleteByExample(example);
    }


    @Override
    public List<PartitionNodeEdgeEntity> findPartitionNodeEdge(String partitionId) {

        return partitionNodeEdgeMapper.queryEdgeByPartitionId(partitionId);
    }

    @Override
    public PageQueryDto<PartitionNodeEdgePairEntity> findPartitionNodeEdgeCallByEdgeId(PageQueryDto<PartitionNodeEdgePairEntity> dto, String edgeId) {
        Page page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        PartitionNodeEdgePairEntity precDemo = new PartitionNodeEdgePairEntity();
        precDemo.setEdgeId(edgeId);
        Example example = new Example(PartitionNodeEdgePairEntity.class);
        example.createCriteria().andEqualTo(precDemo);
        List<PartitionNodeEdgePairEntity> partitionNodeEdgePairEntityList = partitionNodeEdgePairMapper.selectByExample(example);
        for (PartitionNodeEdgePairEntity p :
                partitionNodeEdgePairEntityList) {
            PairRelationEntity pair= pairRelationService.queryCallById(p.getCallId());
            if(pair==null){
                log.error("findPartitionNodeEdgeCallByEdgeId call data wrong");
            }
            p.setPair(pair);
        }
        return dto.addResult(partitionNodeEdgePairEntityList, page.getTotal());
    }

    @Override
    public int countOfPartitionResultEdgeCallByEdgeId(String edgeId) {
        PartitionNodeEdgePairEntity precDemo = new PartitionNodeEdgePairEntity();
        precDemo.setEdgeId(edgeId);
        Example example = new Example(PartitionNodeEdgePairEntity.class);
        example.createCriteria().andEqualTo(precDemo);
        return partitionNodeEdgePairMapper.selectCountByExample(example);
    }

    @Override
    public List<PartitionNodeEdgeEntity> findPartitionResultEdgeByNode(String partitionResultId, String nodeId) {
        PartitionNodeEntity partitionNodeEntity = partitionNodeService.queryPartitionResultById(partitionResultId);
        String partitionId = partitionNodeEntity.getPartitionId();
        PartitionInfoEntity partitionInfoEntity = partitionService.findPartitionById(partitionId);
        List<PairRelationEntity> pairRelationEntityList = pairRelationService.pairRelationsForNode(nodeId, partitionInfoEntity.getPairRelationInfoEntityList());
        List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityList = findPartitionNodeEdge(pairRelationEntityList, partitionId);
        List<PartitionNodeEdgeEntity> result = partitionNodeEdgeEntityList;
//         当一个节点被划分到多个服务中时 需要如下方法进行过滤
        result = new ArrayList<>();
        for (PartitionNodeEdgeEntity partitionResultEdge:
                partitionNodeEdgeEntityList) {
            PartitionNodeEntity pra = partitionNodeService.queryPartitionResultById(partitionResultEdge.getSourceId());
            if(pra.getId().equals(partitionResultId)){
                result.add(partitionResultEdge);
                continue;
            }
            PartitionNodeEntity prb = partitionNodeService.queryPartitionResultById(partitionResultEdge.getTargetId());
            if(prb.getId().equals(partitionResultId)){
                result.add(partitionResultEdge);
            }
        }
        return result;
    }

    @Override
    public List<PartitionNodeEdgeEntity> findPartitionResultEdgeByPartitionResult(String partitionId, String partitionResultId) {
        PartitionNodeEdgeEntity partitionNodeEdgeEntityA = new PartitionNodeEdgeEntity();
        partitionNodeEdgeEntityA.setSourceId(partitionResultId);
        PartitionNodeEdgeEntity partitionNodeEdgeEntityB = new PartitionNodeEdgeEntity();
        partitionNodeEdgeEntityB.setTargetId(partitionResultId);
        Example example = new Example(PartitionNodeEdgeEntity.class);
        example.createCriteria().andEqualTo(partitionNodeEdgeEntityA).orEqualTo(partitionNodeEdgeEntityB);
        return partitionNodeEdgeMapper.selectByExample(example);
    }

    @Override
    public List<PartitionNodeEdgeEntity> findPartitionNodeEdge(List<PairRelationEntity> pairRelationEntityList, String partitionId) {
        Set<String> idList = new HashSet<>();
        for (PairRelationEntity pairRelationEntity :
                pairRelationEntityList) {
            idList.add(pairRelationEntity.getId());
        }

        Example example = new Example(PartitionNodeEdgeEntity.class);
        example.createCriteria().andIn("id", idList);
        return partitionNodeEdgeMapper.selectByExample(example);
    }

    @Override
    public void fillPartitionResultEdgeCall(PartitionNodeEdgeEntity partitionNodeEdgeEntity) {
        PartitionNodeEdgePairEntity precDemo = new PartitionNodeEdgePairEntity();
        precDemo.setEdgeId(partitionNodeEdgeEntity.getId());
        Example example = new Example(PartitionNodeEdgePairEntity.class);
        example.createCriteria().andEqualTo(precDemo);
        List<PartitionNodeEdgePairEntity> partitionNodeEdgePairEntityList = partitionNodeEdgePairMapper.selectByExample(example);
        partitionNodeEdgeEntity.setPartitionNodeEdgePairEntities(partitionNodeEdgePairEntityList);
    }
    @Override
    public void fillPartitionResultEdgeCall(List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityList) {
        for (PartitionNodeEdgeEntity pre :
                partitionNodeEdgeEntityList) {
            fillPartitionResultEdgeCall(pre);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeEdgeCall(String edgeCallId) {
        partitionNodeEdgePairMapper.deleteByPrimaryKey(edgeCallId);
    }
}
