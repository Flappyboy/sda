package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.mock.dto.EdgeDto;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;
import cn.edu.nju.software.sda.app.mock.dto.NodeDto;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.PartitionInfo;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import cn.edu.nju.software.sda.core.domain.work.Work;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.exception.WorkFailedException;
import cn.edu.nju.software.sda.plugin.function.PluginFunction;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.*;

@Service
public class PartitionResultServiceImpl implements PartitionResultService, SdaService {

    @Autowired
    private PartitionResultMapper partitionResultMapper;

    @Autowired
    private PartitionInfoMapper partitionInfoMapper;

    @Autowired
    private PairRelationService pairRelationService;

    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private PartitionResultEdgeService partitionResultEdgeService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private AppService appService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionNodeEntity savePartitionResult(PartitionNodeEntity partitionNodeEntity) {
        String id = Sid.nextShort();
        partitionNodeEntity.setId(id);
        partitionNodeEntity.setCreatedAt(new Date());
        partitionNodeEntity.setUpdatedAt(new Date());
        partitionNodeEntity.setFlag(1);
        partitionResultMapper.insert(partitionNodeEntity);
        return partitionNodeEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartitionResult(PartitionNodeEntity partitionNodeEntity) {
        partitionNodeEntity.setUpdatedAt(new Date());
        partitionResultMapper.updateByPrimaryKeySelective(partitionNodeEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePartitionResult(String partitionResultId) {
        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setId(partitionResultId);
        partitionNodeEntity.setFlag(0);
        partitionNodeEntity.setUpdatedAt(new Date());

        partitionResultMapper.updateByPrimaryKeySelective(partitionNodeEntity);
    }

    @Override
    public PartitionNodeEntity queryPartitionResultById(String partitionResultId) {
        PartitionNodeEntity partitionNodeEntity = partitionResultMapper.selectByPrimaryKey(partitionResultId);
        if(partitionNodeEntity == null || partitionNodeEntity.getFlag().equals(1))
            partitionNodeEntity = null;
        return partitionNodeEntity;
    }

    @Override
    public List<PartitionNodeEntity> queryPartitionResultListPaged(String dynamicInfoId, String algorithmsId, int type, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setFlag(1);
        partitionNodeEntity.setDynamicAnalysisInfoId(dynamicInfoId);
        partitionNodeEntity.setAlgorithmsId(algorithmsId);
        partitionNodeEntity.setType(type);

        Example example = new Example(PartitionNodeEntity.class);
        example.createCriteria().andEqualTo(partitionNodeEntity);
//        example.setOrderByClause("createdAt");

        List<PartitionNodeEntity> partitionNodeEntityList = partitionResultMapper.selectByExample(example);
        return partitionNodeEntityList;
    }

    @Override
    public GraphDto queryPartitionResultForDto(String partitionId) {
        GraphDto graphDto = new GraphDto();

        List<PartitionNodeEntity> partitionNodeEntities = queryPartitionResult(partitionId);
        for (PartitionNodeEntity p : partitionNodeEntities) {
            NodeDto nodeDto = new NodeDto();
            int count = partitionDetailService.countOfPartitionDetailByResultId(p.getId());
            nodeDto.setId(p.getId());
            nodeDto.setName(p.getName());
            nodeDto.setSize(count);
            graphDto.addNode(nodeDto);
        }
        List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityList = partitionResultEdgeService.findPartitionResultEdge(partitionId);
        for (PartitionNodeEdgeEntity p : partitionNodeEdgeEntityList) {
            EdgeDto edgeDto = new EdgeDto();
            edgeDto.setId(p.getId());
            edgeDto.setCount(partitionResultEdgeService.countOfPartitionResultEdgeCallByEdgeId(p.getId()));
            edgeDto.setSource(p.getPatitionResultAId());
            edgeDto.setTarget(p.getPatitionResultBId());
            graphDto.addEdge(edgeDto);
        }
        return graphDto;
    }

    @Override
    public List<PartitionNodeEntity> queryPartitionResult(String partitionId) {
        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setFlag(1);
        partitionNodeEntity.setPartitionId(partitionId);

        Example example = new Example(PartitionNodeEntity.class);
        example.createCriteria().andEqualTo(partitionNodeEntity);
        List<PartitionNodeEntity> partitionNodeEntityList = partitionResultMapper.selectByExample(example);
        return partitionNodeEntityList;
    }

    @Override
    public List<String> findPartitionResultIds(String partitionId) {
        List<PartitionNodeEntity> partitionNodeEntityList = queryPartitionResult(partitionId);
        List<String> idList = new ArrayList<>();
        for (PartitionNodeEntity pr :
                partitionNodeEntityList) {
            idList.add(pr.getId());
        }
        return idList;
    }

    @Override
    public PartitionNodeEntity queryPartitionResult(String partitionId, String partitionResultName) {
        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setFlag(1);
        partitionNodeEntity.setPartitionId(partitionId);
        partitionNodeEntity.setName(partitionResultName);

        Example example = new Example(PartitionNodeEntity.class);
        example.createCriteria().andEqualTo(partitionNodeEntity);
        List<PartitionNodeEntity> partitionNodeEntityList = partitionResultMapper.selectByExample(example);

        if(partitionNodeEntityList == null || partitionNodeEntityList.size() <= 0)
            return null;
        return partitionNodeEntityList.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void partition(PartitionInfoEntity partitionInfoEntity) {
        String appid = partitionInfoEntity.getAppId();
        String algorithmsid = partitionInfoEntity.getAlgorithmsId();
        String dynamicanalysisinfoid = partitionInfoEntity.getDynamicAnalysisinfoId();
        int type = partitionInfoEntity.getType();
        String partitionId = partitionInfoEntity.getId();

        PluginFunction pa = PluginFunctionManager.get(algorithmsid);

        List<String> infoIdList = new ArrayList<>();
        infoIdList.add(dynamicanalysisinfoid);
        App app = appService.getAppWithInfo(appid, infoIdList);

        Partition partition = null;
        File workspace = WorkspaceUtil.workspace("partition");
        Work work = new Work();
        work.setWorkspace(workspace);
        InputData inputData = new InputData();
        // TODO 向inputdata中输入值

        try {
            partition = pa.work(inputData, work).getInfoByClass(PartitionInfo.class).getPartition();
        } catch (WorkFailedException e) {
            e.printStackTrace();
        }

        int communityCount = 0;
        if(partition != null) {
            for (PartitionNode pn : partition.getPartitionNodeSet()) {

                communityCount += 1;
                PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
                partitionNodeEntity.setAlgorithmsId(algorithmsid);
                partitionNodeEntity.setAppId(appid);
                partitionNodeEntity.setDesc("community: " + communityCount);
                partitionNodeEntity.setDynamicAnalysisInfoId(dynamicanalysisinfoid);
                partitionNodeEntity.setName(String.valueOf(communityCount));
                partitionNodeEntity.setOrder(communityCount);
                partitionNodeEntity.setType(type);
                partitionNodeEntity.setPartitionId(partitionId);
                PartitionNodeEntity pr = savePartitionResult(partitionNodeEntity);

                for (Node node :
                        pn.getNodeSet()) {

                    PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
                    partitionDetailEntity.setDesc(pr.getDesc() + "的结点");
                    partitionDetailEntity.setNodeId(node.getId());
                    partitionDetailEntity.setPatitionResultId(pr.getId());
                    partitionDetailEntity.setType(type);
                    partitionDetailService.savePartitionDetail(partitionDetailEntity);
                }
            }
        }

        FileUtil.delete(workspace);

        partitionResultEdgeService.statisticsPartitionResultEdge(partitionInfoEntity);

        PartitionInfoEntity newPartitionInfoEntity = new PartitionInfoEntity();
        newPartitionInfoEntity.setId(partitionId);
        newPartitionInfoEntity.setStatus(1);
        partitionInfoMapper.updateByPrimaryKeySelective(newPartitionInfoEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfPartitionResult(String dynamicInfoId, String algorithmsId, int type) {
        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setAlgorithmsId(algorithmsId);
        partitionNodeEntity.setDynamicAnalysisInfoId(dynamicInfoId);
        partitionNodeEntity.setType(type);
        partitionNodeEntity.setFlag(1);
        return partitionResultMapper.selectCount(partitionNodeEntity);
    }

    @Override
    public int countOfPartitionResult(String id) {
        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setPartitionId(id);
        partitionNodeEntity.setFlag(1);
        return partitionResultMapper.selectCount(partitionNodeEntity);
    }

    @Override
    public String getName() {
        return "partition";
    }
}
