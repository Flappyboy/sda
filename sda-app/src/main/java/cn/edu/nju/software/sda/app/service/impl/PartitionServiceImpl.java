package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.exception.WorkFailedException;
import cn.edu.nju.software.sda.plugin.function.PluginFunction;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
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
    public PartitionInfoEntity findPartitionById(String partitionId) {
        return partitionMapper.selectByPrimaryKey(partitionId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addPartition(PartitionInfoEntity partition) {
        String id = Sid.nextShort();
        partition.setId(id);
        partition.setCreatedAt(new Date());
        partition.setUpdatedAt(new Date());
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
    public List<HashMap<String, Object>> findBycondition(Integer page, Integer pageSize, String algorithmsid, Integer type) {
        // 开始分页
        PageHelper.startPage(page, pageSize);

        Example example = new Example(PartitionInfoEntity.class);
        PartitionInfoEntity demo = new PartitionInfoEntity();
        demo.setFlag(1);
        if(StringUtils.isNoneBlank(algorithmsid))
            demo.setAlgorithmsId(algorithmsid);
        demo.setType(type);
        example.createCriteria().andEqualTo(demo);
        example.setOrderByClause("createdAt desc");
        List<PartitionInfoEntity> partitionList = partitionMapper.selectByExample(example);
        List<HashMap<String, Object>> results = new ArrayList<>();

        for (PartitionInfoEntity partitionInfoEntity : partitionList) {
            HashMap<String, Object> result = new HashMap<>();
            String appid = partitionInfoEntity.getAppId();
            AppEntity app = appMapper.selectByPrimaryKey(appid);
            String algoid = partitionInfoEntity.getAlgorithmsId();

            result.put("id", partitionInfoEntity.getId());
            if (app != null)
                result.put("appName", app.getName());
            result.put("dynamicAnalysisInfoId", partitionInfoEntity.getDynamicAnalysisinfoId());
            if (algoid != null)
                result.put("algorithmsName", algoid);
            result.put("desc", partitionInfoEntity.getDesc());
            result.put("status", partitionInfoEntity.getStatus());
            result.put("type", partitionInfoEntity.getType());
            result.put("createdAt", partitionInfoEntity.getCreatedAt());
            result.put("updatedAt", partitionInfoEntity.getUpdatedAt());
            results.add(result);

        }
        return results;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int count(String algorithmsid, Integer type) {
        Example example = new Example(PartitionInfoEntity.class);
        PartitionInfoEntity demo = new PartitionInfoEntity();
        demo.setFlag(1);
        if(StringUtils.isNoneBlank(algorithmsid))
            demo.setAlgorithmsId(algorithmsid);
        demo.setType(type);
        example.createCriteria().andEqualTo(demo);
        int count = partitionMapper.selectCountByExample(example);
        return count;
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
        String appId = partitionInfo.getAppId();
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
            pd.setPatitionResultId(partitionResult.getId());
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
        PluginFunction ep = PluginFunctionManager.get(evaluationPluginName);
        File workspace = WorkspaceUtil.workspace("partition");
        Work work = new Work();
        work.setWorkspace(workspace);
        Evaluation evaluation = null;
        InputData inputData = new InputData();
        // 向InputData中传值

        try {
            evaluation = ((EvaluationInfo)ep.work(inputData, work).getInfoByName(EvaluationInfo.INFO_NAME_EVALUATION)).getEvaluation();
        } catch (WorkFailedException e) {
            e.printStackTrace();
        } finally {
            FileUtil.delete(workspace);
        }
        return evaluation;
    }
}
