package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.PartitionResultEdgeCallMapper;
import cn.edu.nju.software.sda.app.dao.PartitionResultEdgeMapper;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
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
    public void statisticsPartitionResultEdge(PartitionInfo partitionInfo) {
        clearPartitionResultEdge(partitionInfo);

//        PartitionInfo partitionInfo = partitionService.findPartitionById(partitionId);
        List<PartitionResultEdge> partitionResultEdgeFromStaticList = partitionResultEdgeMapper.statisticsEdgesFromStatic(partitionInfo.getId(), partitionInfo.getAppid());
        for (PartitionResultEdge p: partitionResultEdgeFromStaticList) {
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
                PartitionResultEdgeCall partitionResultEdgeCall = new PartitionResultEdgeCall();
                partitionResultEdgeCall.setId(Sid.nextShort());
                partitionResultEdgeCall.setCallid(staticCallInfo.getId());
                partitionResultEdgeCall.setEdgeid(p.getId());
                partitionResultEdgeCall.setCreatedat(new Date());
                partitionResultEdgeCall.setUpdatedat(new Date());
                partitionResultEdgeCallMapper.insert(partitionResultEdgeCall);
            }
        }

        List<PartitionResultEdge> partitionResultEdgeFromDynamicList = partitionResultEdgeMapper.statisticsEdgesFromDynamic(partitionInfo.getId(), partitionInfo.getDynamicanalysisinfoid());
        for (PartitionResultEdge dynamicEdge: partitionResultEdgeFromDynamicList) {
            if(dynamicEdge.getPatitionResultAId() == null || dynamicEdge.getPatitionResultBId() == null)
                continue;
            if(dynamicEdge.getPatitionResultAId().equals(dynamicEdge.getPatitionResultBId()))
                continue;
            boolean continueFlag = false;
            for (int i=0; i<partitionResultEdgeFromStaticList.size(); i++){
                PartitionResultEdge staticEdge = partitionResultEdgeFromStaticList.get(i);
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
                        PartitionResultEdgeCall partitionResultEdgeCall = new PartitionResultEdgeCall();
                        partitionResultEdgeCall.setId(Sid.nextShort());
                        partitionResultEdgeCall.setCallid(dynamicCallInfo.getId());
                        partitionResultEdgeCall.setEdgeid(dynamicEdge.getId());
                        partitionResultEdgeCall.setCreatedat(new Date());
                        partitionResultEdgeCall.setUpdatedat(new Date());
                        partitionResultEdgeCallMapper.insert(partitionResultEdgeCall);
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
                PartitionResultEdgeCall partitionResultEdgeCall = new PartitionResultEdgeCall();
                partitionResultEdgeCall.setId(Sid.nextShort());
                partitionResultEdgeCall.setCallid(dynamicCallInfo.getId());
                partitionResultEdgeCall.setEdgeid(dynamicEdge.getId());
                partitionResultEdgeCall.setCreatedat(new Date());
                partitionResultEdgeCall.setUpdatedat(new Date());
                partitionResultEdgeCallMapper.insert(partitionResultEdgeCall);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void clearPartitionResultEdge(PartitionInfo partitionInfo) {
        // TODO
        // 级联删除edgeCall

        //删除edges
        List<String> idList = partitionResultService.findPartitionResultIds(partitionInfo.getId());
        if(idList.size() == 0)
            return;
        Example example = new Example(PartitionResultEdge.class);
        example.createCriteria().andIn("patitionResultAId",idList);
        partitionResultEdgeMapper.deleteByExample(example);
    }


    @Override
    public List<PartitionResultEdge> findPartitionResultEdge(String partitionId) {

        return partitionResultEdgeMapper.queryEdgeByPartitionId(partitionId);
    }

    @Override
    public List<PartitionResultEdgeCall> findPartitionResultEdgeCallByEdgeId(String edgeId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        PartitionResultEdgeCall precDemo = new PartitionResultEdgeCall();
        precDemo.setEdgeid(edgeId);
        Example example = new Example(PartitionResultEdgeCall.class);
        example.createCriteria().andEqualTo(precDemo);
        List<PartitionResultEdgeCall> partitionResultEdgeCallList = partitionResultEdgeCallMapper.selectByExample(example);
        for (PartitionResultEdgeCall p :
                partitionResultEdgeCallList) {
//                DynamicCallInfo dynamicCallInfo = dynamicCallInfoMapper.selectByPrimaryKey(p.getCallid());
            PairRelationEntity call= pairRelationService.queryCallById(p.getCallid());
            if(call==null){
                log.error("findPartitionResultEdgeCallByEdgeId call data wrong");
            }
            p.setCall(call);
        }
        return partitionResultEdgeCallList;
    }

    @Override
    public int countOfPartitionResultEdgeCallByEdgeId(String edgeId) {
        PartitionResultEdgeCall precDemo = new PartitionResultEdgeCall();
        precDemo.setEdgeid(edgeId);
        Example example = new Example(PartitionResultEdgeCall.class);
        example.createCriteria().andEqualTo(precDemo);
        return partitionResultEdgeCallMapper.selectCountByExample(example);
    }

    @Override
    public List<PartitionResultEdge> findPartitionResultEdgeByNode(String partitionResultId, String nodeId) {
        PartitionResult partitionResult = partitionResultService.queryPartitionResultById(partitionResultId);
        String partitionId = partitionResult.getPartitionId();
        PartitionInfo partitionInfo = partitionService.findPartitionById(partitionId);
        List<PairRelationEntity> pairRelationEntityList = pairRelationService.pairRelationsForNode(nodeId, Arrays.asList(partitionInfo.getDynamicanalysisinfoid()));
        List<PartitionResultEdge> partitionResultEdgeList = findPartitionResultEdge(pairRelationEntityList, partitionId);
        List<PartitionResultEdge> result = partitionResultEdgeList;
        /* 当一个节点被划分到多个服务中时 需要如下方法进行过滤
        result = new ArrayList<>();
        for (PartitionResultEdge partitionResultEdge:
                partitionResultEdgeList) {
            PartitionResult pra = partitionResultService.queryPartitionResultById(partitionResultEdge.getPatitionResultAId());
            if(pra.getId().equals(partitionResultId)){
                result.add(partitionResultEdge);
                continue;
            }
            PartitionResult prb = partitionResultService.queryPartitionResultById(partitionResultEdge.getPatitionResultBId());
            if(prb.getId().equals(partitionResultId)){
                result.add(partitionResultEdge);
            }
        }*/
        return result;
    }

    @Override
    public List<PartitionResultEdge> findPartitionResultEdgeByPartitionResult(String partitionId, String partitionResultId) {
        PartitionResultEdge partitionResultEdgeA = new PartitionResultEdge();
        partitionResultEdgeA.setPatitionResultAId(partitionResultId);
        PartitionResultEdge partitionResultEdgeB = new PartitionResultEdge();
        partitionResultEdgeB.setPatitionResultBId(partitionResultId);
        Example example = new Example(PartitionResultEdge.class);
        example.createCriteria().andEqualTo(partitionResultEdgeA).orEqualTo(partitionResultEdgeB);
        return partitionResultEdgeMapper.selectByExample(example);
    }

    @Override
    public List<PartitionResultEdge> findPartitionResultEdge(List<PairRelationEntity> pairRelationEntityList, String partitionId) {
        Set<String> idList = new HashSet<>();
        for (PairRelationEntity pairRelationEntity :
                pairRelationEntityList) {
            idList.add(pairRelationEntity.getId());
        }

        Example example = new Example(PartitionResultEdge.class);
        example.createCriteria().andIn("id", idList);
        return partitionResultEdgeMapper.selectByExample(example);
    }

    @Override
    public void fillPartitionResultEdgeCall(PartitionResultEdge partitionResultEdge) {
        PartitionResultEdgeCall precDemo = new PartitionResultEdgeCall();
        precDemo.setEdgeid(partitionResultEdge.getId());
        Example example = new Example(PartitionResultEdgeCall.class);
        example.createCriteria().andEqualTo(precDemo);
        List<PartitionResultEdgeCall> partitionResultEdgeCallList = partitionResultEdgeCallMapper.selectByExample(example);
        partitionResultEdge.setPartitionResultEdgeCallList(partitionResultEdgeCallList);
    }
    @Override
    public void fillPartitionResultEdgeCall(List<PartitionResultEdge> partitionResultEdgeList) {
        for (PartitionResultEdge pre :
                partitionResultEdgeList) {
            fillPartitionResultEdgeCall(pre);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeEdgeCall(String edgeCallId) {
        partitionResultEdgeCallMapper.deleteByPrimaryKey(edgeCallId);
    }
}
