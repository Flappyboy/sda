package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.PartitionResultEdgeCallMapper;
import cn.edu.nju.software.sda.app.dao.PartitionResultEdgeMapper;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
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
public class PartitionResultEdgeServiceImpl implements PartitionResultEdgeService {

    @Autowired
    private PartitionResultEdgeMapper partitionResultEdgeMapper;

    @Autowired
    private PartitionResultEdgeCallMapper partitionResultEdgeCallMapper;

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private PartitionResultService partitionResultService;

    @Autowired
    private PairRelationService pairRelationService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void statisticsPartitionResultEdge(PartitionInfoEntity partitionInfoEntity) {
        clearPartitionResultEdge(partitionInfoEntity);

//        PartitionInfoEntity partitionInfoEntity = partitionService.findPartitionById(partitionId);
        List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityFromStaticList = partitionResultEdgeMapper.statisticsEdgesFromStatic(partitionInfoEntity.getId(), partitionInfoEntity.getAppId());
        for (PartitionNodeEdgeEntity p: partitionNodeEdgeEntityFromStaticList) {
            if(p.getPatitionResultAId() == null || p.getPatitionResultBId() == null)
                continue;
            if(p.getPatitionResultAId().equals(p.getPatitionResultBId()))
                continue;
            p.setId(Sid.nextShort());
            p.setCreatedAt(new Date());
            p.setUpdatedAt(new Date());
            partitionResultEdgeMapper.insert(p);
            List<PairRelationEntity> staticCallInfoList = p.getStaticCallInfoList();
            for(PairRelationEntity staticCallInfo: staticCallInfoList){
                PartitionNodeEdgePairEntity partitionNodeEdgePairEntity = new PartitionNodeEdgePairEntity();
                partitionNodeEdgePairEntity.setId(Sid.nextShort());
                partitionNodeEdgePairEntity.setCallid(staticCallInfo.getId());
                partitionNodeEdgePairEntity.setEdgeid(p.getId());
                partitionNodeEdgePairEntity.setCreatedat(new Date());
                partitionNodeEdgePairEntity.setUpdatedat(new Date());
                partitionResultEdgeCallMapper.insert(partitionNodeEdgePairEntity);
            }
        }

        List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityFromDynamicList = partitionResultEdgeMapper.statisticsEdgesFromDynamic(partitionInfoEntity.getId(), partitionInfoEntity.getDynamicAnalysisinfoId());
        for (PartitionNodeEdgeEntity dynamicEdge: partitionNodeEdgeEntityFromDynamicList) {
            if(dynamicEdge.getPatitionResultAId() == null || dynamicEdge.getPatitionResultBId() == null)
                continue;
            if(dynamicEdge.getPatitionResultAId().equals(dynamicEdge.getPatitionResultBId()))
                continue;
            boolean continueFlag = false;
            for (int i = 0; i< partitionNodeEdgeEntityFromStaticList.size(); i++){
                PartitionNodeEdgeEntity staticEdge = partitionNodeEdgeEntityFromStaticList.get(i);
                if(staticEdge.getPatitionResultAId().equals(dynamicEdge.getPatitionResultBId()) && staticEdge.getPatitionResultBId().equals(dynamicEdge.getPatitionResultBId())){
                    dynamicEdge.setId(staticEdge.getId());
                    List<PairRelationEntity> dynamicCallInfoList = dynamicEdge.getDynamicCallInfoList();
                    for(PairRelationEntity dynamicCallInfo: dynamicCallInfoList){
                        List<PairRelationEntity> staticCallInfoList = staticEdge.getStaticCallInfoList();
                        boolean continueFlag2 = false;
                        for(PairRelationEntity staticCallInfo: staticCallInfoList){
                            if(staticCallInfo.getSourceNode().equals(dynamicCallInfo.getSourceNode()) && staticCallInfo.getTargetNode().equals(dynamicCallInfo.getTargetNode())) {
                                continueFlag2 = true;
                                break;
                            }
                        }
                        if (continueFlag2){
                            continue;
                        }
                        PartitionNodeEdgePairEntity partitionNodeEdgePairEntity = new PartitionNodeEdgePairEntity();
                        partitionNodeEdgePairEntity.setId(Sid.nextShort());
                        partitionNodeEdgePairEntity.setCallid(dynamicCallInfo.getId());
                        partitionNodeEdgePairEntity.setEdgeid(dynamicEdge.getId());
                        partitionNodeEdgePairEntity.setCreatedat(new Date());
                        partitionNodeEdgePairEntity.setUpdatedat(new Date());
                        partitionResultEdgeCallMapper.insert(partitionNodeEdgePairEntity);
                    }
                    continueFlag=true;
                    break;
                }else{
                    continue;
                }
            }
            if(continueFlag)
                continue;

            dynamicEdge.setId(Sid.nextShort());
            dynamicEdge.setCreatedAt(new Date());
            dynamicEdge.setUpdatedAt(new Date());
            partitionResultEdgeMapper.insert(dynamicEdge);
            List<PairRelationEntity> dynamicCallInfoList = dynamicEdge.getDynamicCallInfoList();
            for(PairRelationEntity dynamicCallInfo: dynamicCallInfoList){
                PartitionNodeEdgePairEntity partitionNodeEdgePairEntity = new PartitionNodeEdgePairEntity();
                partitionNodeEdgePairEntity.setId(Sid.nextShort());
                partitionNodeEdgePairEntity.setCallid(dynamicCallInfo.getId());
                partitionNodeEdgePairEntity.setEdgeid(dynamicEdge.getId());
                partitionNodeEdgePairEntity.setCreatedat(new Date());
                partitionNodeEdgePairEntity.setUpdatedat(new Date());
                partitionResultEdgeCallMapper.insert(partitionNodeEdgePairEntity);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void clearPartitionResultEdge(PartitionInfoEntity partitionInfoEntity) {
        // TODO
        // 级联删除edgeCall

        //删除edges
        List<String> idList = partitionResultService.findPartitionResultIds(partitionInfoEntity.getId());
        if(idList.size() == 0)
            return;
        Example example = new Example(PartitionNodeEdgeEntity.class);
        example.createCriteria().andIn("patitionResultAId",idList);
        partitionResultEdgeMapper.deleteByExample(example);
    }


    @Override
    public List<PartitionNodeEdgeEntity> findPartitionResultEdge(String partitionId) {

        return partitionResultEdgeMapper.queryEdgeByPartitionId(partitionId);
    }

    @Override
    public List<PartitionNodeEdgePairEntity> findPartitionResultEdgeCallByEdgeId(String edgeId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        PartitionNodeEdgePairEntity precDemo = new PartitionNodeEdgePairEntity();
        precDemo.setEdgeid(edgeId);
        Example example = new Example(PartitionNodeEdgePairEntity.class);
        example.createCriteria().andEqualTo(precDemo);
        List<PartitionNodeEdgePairEntity> partitionNodeEdgePairEntityList = partitionResultEdgeCallMapper.selectByExample(example);
        for (PartitionNodeEdgePairEntity p :
                partitionNodeEdgePairEntityList) {
//                DynamicCallInfo dynamicCallInfo = dynamicCallInfoMapper.selectByPrimaryKey(p.getCallid());
            PairRelationEntity call= pairRelationService.queryCallById(p.getCallid());
            if(call==null){
                log.error("findPartitionResultEdgeCallByEdgeId call data wrong");
            }
            p.setCall(call);
        }
        return partitionNodeEdgePairEntityList;
    }

    @Override
    public int countOfPartitionResultEdgeCallByEdgeId(String edgeId) {
        PartitionNodeEdgePairEntity precDemo = new PartitionNodeEdgePairEntity();
        precDemo.setEdgeid(edgeId);
        Example example = new Example(PartitionNodeEdgePairEntity.class);
        example.createCriteria().andEqualTo(precDemo);
        return partitionResultEdgeCallMapper.selectCountByExample(example);
    }

    @Override
    public List<PartitionNodeEdgeEntity> findPartitionResultEdgeByNode(String partitionResultId, String nodeId) {
        PartitionNodeEntity partitionNodeEntity = partitionResultService.queryPartitionResultById(partitionResultId);
        String partitionId = partitionNodeEntity.getPartitionId();
        PartitionInfoEntity partitionInfoEntity = partitionService.findPartitionById(partitionId);
        List<PairRelationEntity> pairRelationEntityList = pairRelationService.pairRelationsForNode(nodeId, Arrays.asList(partitionInfoEntity.getDynamicAnalysisinfoId()));
        List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityList = findPartitionResultEdge(pairRelationEntityList, partitionId);
        List<PartitionNodeEdgeEntity> result = partitionNodeEdgeEntityList;
        /* 当一个节点被划分到多个服务中时 需要如下方法进行过滤
        result = new ArrayList<>();
        for (PartitionNodeEdgeEntity partitionResultEdge:
                partitionNodeEdgeEntityList) {
            PartitionNodeEntity pra = partitionResultService.queryPartitionResultById(partitionResultEdge.getPatitionResultAId());
            if(pra.getId().equals(partitionResultId)){
                result.add(partitionResultEdge);
                continue;
            }
            PartitionNodeEntity prb = partitionResultService.queryPartitionResultById(partitionResultEdge.getPatitionResultBId());
            if(prb.getId().equals(partitionResultId)){
                result.add(partitionResultEdge);
            }
        }*/
        return result;
    }

    @Override
    public List<PartitionNodeEdgeEntity> findPartitionResultEdgeByPartitionResult(String partitionId, String partitionResultId) {
        PartitionNodeEdgeEntity partitionNodeEdgeEntityA = new PartitionNodeEdgeEntity();
        partitionNodeEdgeEntityA.setPatitionResultAId(partitionResultId);
        PartitionNodeEdgeEntity partitionNodeEdgeEntityB = new PartitionNodeEdgeEntity();
        partitionNodeEdgeEntityB.setPatitionResultBId(partitionResultId);
        Example example = new Example(PartitionNodeEdgeEntity.class);
        example.createCriteria().andEqualTo(partitionNodeEdgeEntityA).orEqualTo(partitionNodeEdgeEntityB);
        return partitionResultEdgeMapper.selectByExample(example);
    }

    @Override
    public List<PartitionNodeEdgeEntity> findPartitionResultEdge(List<PairRelationEntity> pairRelationEntityList, String partitionId) {
        Set<String> idList = new HashSet<>();
        for (PairRelationEntity pairRelationEntity :
                pairRelationEntityList) {
            idList.add(pairRelationEntity.getId());
        }

        Example example = new Example(PartitionNodeEdgeEntity.class);
        example.createCriteria().andIn("id", idList);
        return partitionResultEdgeMapper.selectByExample(example);
    }

    @Override
    public void fillPartitionResultEdgeCall(PartitionNodeEdgeEntity partitionNodeEdgeEntity) {
        PartitionNodeEdgePairEntity precDemo = new PartitionNodeEdgePairEntity();
        precDemo.setEdgeid(partitionNodeEdgeEntity.getId());
        Example example = new Example(PartitionNodeEdgePairEntity.class);
        example.createCriteria().andEqualTo(precDemo);
        List<PartitionNodeEdgePairEntity> partitionNodeEdgePairEntityList = partitionResultEdgeCallMapper.selectByExample(example);
        partitionNodeEdgeEntity.setPartitionNodeEdgePairEntityList(partitionNodeEdgePairEntityList);
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
        partitionResultEdgeCallMapper.deleteByPrimaryKey(edgeCallId);
    }
}
