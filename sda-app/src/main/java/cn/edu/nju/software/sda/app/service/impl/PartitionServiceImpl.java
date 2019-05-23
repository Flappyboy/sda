package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.evaluation.EvaluationAlgorithmManager;
import cn.edu.nju.software.sda.plugin.evaluation.EvaluationAlgorithm;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class PartitionServiceImpl implements PartitionService {
    @Autowired
    private PartitionInfoMapper partitionMapper;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private PartitionResultMapper partitionResultMapper;

    @Autowired
    private PairRelationMapper pairRelationMapper;

    @Autowired
    private PairRelationService pairRelationService;

    @Autowired
    private PartitionDetailMapper partitionDetailMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private PartitionResultService partitionResultService;

    @Autowired
    private AppService appService;

    @Override
    public PartitionInfo findPartitionById(String partitionId) {
        return partitionMapper.selectByPrimaryKey(partitionId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addPartition(PartitionInfo partition) {
        String id = Sid.nextShort();
        partition.setId(id);
        partition.setCreatedat(new Date());
        partition.setUpdatedat(new Date());
        partition.setFlag(1);
        partition.setStatus(0);
        System.out.println(partition);
        partitionMapper.insertSelective(partition);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.debug("partion start: "+partition);
                    partitionResultService.partition(partition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delPartition(String id) {
        PartitionInfo partition = new PartitionInfo();
        partition.setId(id);
        partition.setFlag(0);
        updatePartition(partition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartition(PartitionInfo partition) {
        partition.setUpdatedat(new Date());
        partitionMapper.updateByPrimaryKeySelective(partition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<HashMap<String, Object>> findBycondition(Integer page, Integer pageSize, String algorithmsid, Integer type) {
        // 开始分页
        PageHelper.startPage(page, pageSize);

        PartitionInfoExample example = new PartitionInfoExample();
        PartitionInfoExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);

        if (algorithmsid != "" && algorithmsid != null && !algorithmsid.isEmpty())
            criteria.andAlgorithmsidEqualTo(algorithmsid);
        if (type != null)
            criteria.andTypeEqualTo(type);
        example.setOrderByClause("createdAt desc");
        List<PartitionInfo> partitionList = partitionMapper.selectByExample(example);
        List<HashMap<String, Object>> results = new ArrayList<>();

        for (PartitionInfo partitionInfo : partitionList) {
            HashMap<String, Object> result = new HashMap<>();
            String appid = partitionInfo.getAppid();
            AppEntity app = appMapper.selectByPrimaryKey(appid);
            String algoid = partitionInfo.getAlgorithmsid();

            result.put("id", partitionInfo.getId());
            if (app != null)
                result.put("appName", app.getName());
            result.put("dynamicAnalysisInfoId", partitionInfo.getDynamicanalysisinfoid());
            if (algoid != null)
                result.put("algorithmsName", algoid);
            result.put("desc", partitionInfo.getDesc());
            result.put("status", partitionInfo.getStatus());
            result.put("type", partitionInfo.getType());
            result.put("createdAt", partitionInfo.getCreatedat());
            result.put("updatedAt", partitionInfo.getUpdatedat());
            results.add(result);

        }
        return results;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int count(String algorithmsid, Integer type) {
        PartitionInfoExample example = new PartitionInfoExample();
        PartitionInfoExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);

        if (algorithmsid != "" && algorithmsid != null && !algorithmsid.isEmpty())
            criteria.andAlgorithmsidEqualTo(algorithmsid);
        if (type != null)
            criteria.andTypeEqualTo(type);
        int count = partitionMapper.countByExample(example);
        return count;
    }
/*

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraph getGraph(String partitionInfoId) {
        PartitionGraph partitionGraph = new PartitionGraph();

        //找出所有社区
        PartitionInfo partitionInfo = partitionMapper.selectByPrimaryKey(partitionInfoId);
        if(partitionInfo == null)
            return  null;
        int type = partitionInfo.getType();
        String appId = partitionInfo.getAppId();
        String namicanalysisinfoid = partitionInfo.getDynamicanalysisinfoid();


        Example example = new Example(PartitionResult.class);
        PartitionResult partitionResultExample = new PartitionResult();
        partitionResultExample.setPartitionId(partitionInfoId);
        partitionResultExample.setFlag(1);
        example.createCriteria().andEqualTo(partitionResultExample);
        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);

        HashMap<String, List<String>> nodes = new HashMap<>();

        //社区节点
        List<PartitionGraphNode> partitionNodes = new ArrayList<>();
        for (PartitionResult partitionResult : partitionResultList) {
            List<String> nodeIds = new ArrayList<>();
            PartitionGraphNode partitionNode = new PartitionGraphNode();
            partitionNode.setCommunity(partitionResult);
            List<NodeEntity> nodeEntities = new ArrayList<>();

            PartitionDetail pd = new PartitionDetail();
            pd.setPatitionResultId(partitionResult.getId());
            pd.setFlag(1);
            Example pdExample = new Example(PartitionDetail.class);
            example.createCriteria().andEqualTo(pd);

            List<PartitionDetail> partitionDetails = partitionDetailMapper.selectByExample(pdExample);
            for (PartitionDetail partitionDetail : partitionDetails) {
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
                    staticCallInfos = getStaticEdge(nodes.get(communityId1),nodes.get(communityId2),type,appId);
                    staticCallInfos.addAll(getStaticEdge(nodes.get(communityId2),nodes.get(communityId1),type,appId));
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

    @Override
    public Evaluation evaluate(String partitionId, String evaluationPluginName) {
        App app = appService.getAppWithPartition(partitionId);
        EvaluationAlgorithm ep = EvaluationAlgorithmManager.get(evaluationPluginName);
        File workspace = WorkspaceUtil.workspace("partition");
        Evaluation evaluation = null;
        try {
            evaluation = ep.evaluate(app, workspace);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtil.delete(workspace);
        }
        return evaluation;
    }
}
