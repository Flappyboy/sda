package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.mock.dto.EdgeDto;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;
import cn.edu.nju.software.sda.app.mock.dto.NodeDto;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import cn.edu.nju.software.sda.core.utils.FileUtil;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithmManager;
import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
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
    public PartitionResult savePartitionResult(PartitionResult partitionResult) {
        String id = Sid.nextShort();
        partitionResult.setId(id);
        partitionResult.setCreatedAt(new Date());
        partitionResult.setUpdatedAt(new Date());
        partitionResult.setFlag(1);
        partitionResultMapper.insert(partitionResult);
        return partitionResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartitionResult(PartitionResult partitionResult) {
        partitionResult.setUpdatedAt(new Date());
        partitionResultMapper.updateByPrimaryKeySelective(partitionResult);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePartitionResult(String partitionResultId) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setId(partitionResultId);
        partitionResult.setFlag(0);
        partitionResult.setUpdatedAt(new Date());

        partitionResultMapper.updateByPrimaryKeySelective(partitionResult);
    }

    @Override
    public PartitionResult queryPartitionResultById(String partitionResultId) {
        PartitionResult partitionResult = partitionResultMapper.selectByPrimaryKey(partitionResultId);
        if(partitionResult == null || partitionResult.getFlag().equals(1))
            partitionResult = null;
        return partitionResult;
    }

    @Override
    public List<PartitionResult> queryPartitionResultListPaged(String dynamicInfoId, String algorithmsId, int type, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setFlag(1);
        partitionResult.setDynamicAnalysisInfoId(dynamicInfoId);
        partitionResult.setAlgorithmsId(algorithmsId);
        partitionResult.setType(type);

        Example example = new Example(PartitionResult.class);
        example.createCriteria().andEqualTo(partitionResult);
//        example.setOrderByClause("createdAt");

        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);
        return partitionResultList;
    }

    @Override
    public GraphDto queryPartitionResultForDto(String partitionId) {
        GraphDto graphDto = new GraphDto();

        List<PartitionResult> partitionResults = queryPartitionResult(partitionId);
        for (PartitionResult p : partitionResults) {
            NodeDto nodeDto = new NodeDto();
            int count = partitionDetailService.countOfPartitionDetailByResultId(p.getId());
            nodeDto.setId(p.getId());
            nodeDto.setName(p.getName());
            nodeDto.setSize(count);
            graphDto.addNode(nodeDto);
        }
        List<PartitionResultEdge> partitionResultEdgeList = partitionResultEdgeService.findPartitionResultEdge(partitionId);
        for (PartitionResultEdge p : partitionResultEdgeList) {
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
    public List<PartitionResult> queryPartitionResult(String partitionId) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setFlag(1);
        partitionResult.setPartitionId(partitionId);

        Example example = new Example(PartitionResult.class);
        example.createCriteria().andEqualTo(partitionResult);
        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);
        return partitionResultList;
    }

    @Override
    public List<String> findPartitionResultIds(String partitionId) {
        List<PartitionResult> partitionResultList = queryPartitionResult(partitionId);
        List<String> idList = new ArrayList<>();
        for (PartitionResult pr :
                partitionResultList) {
            idList.add(pr.getId());
        }
        return idList;
    }

    @Override
    public PartitionResult queryPartitionResult(String partitionId, String partitionResultName) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setFlag(1);
        partitionResult.setPartitionId(partitionId);
        partitionResult.setName(partitionResultName);

        Example example = new Example(PartitionResult.class);
        example.createCriteria().andEqualTo(partitionResult);
        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);

        if(partitionResultList == null || partitionResultList.size() <= 0)
            return null;
        return partitionResultList.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void partition(PartitionInfo partitionInfo) {
        String appid = partitionInfo.getAppid();
        String algorithmsid = partitionInfo.getAlgorithmsid();
        String dynamicanalysisinfoid = partitionInfo.getDynamicanalysisinfoid();
        int type = partitionInfo.getType();
        String partitionId = partitionInfo.getId();

        PartitionPlugin pa = PartitionAlgorithmManager.get(algorithmsid);

        List<String> infoIdList = new ArrayList<>();
        infoIdList.add(dynamicanalysisinfoid);
        App app = appService.getAppWithInfo(appid, infoIdList);

        Partition partition = null;
        File workspace = WorkspaceUtil.workspace("partition");
        try {
            partition = pa.partition(app, workspace);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int communityCount = 0;
        if(partition != null) {
            for (PartitionNode pn : partition.getPartitionNodeSet()) {

                communityCount += 1;
                PartitionResult partitionResult = new PartitionResult();
                partitionResult.setAlgorithmsId(algorithmsid);
                partitionResult.setAppId(appid);
                partitionResult.setDesc("community: " + communityCount);
                partitionResult.setDynamicAnalysisInfoId(dynamicanalysisinfoid);
                partitionResult.setName(String.valueOf(communityCount));
                partitionResult.setOrder(communityCount);
                partitionResult.setType(type);
                partitionResult.setPartitionId(partitionId);
                PartitionResult pr = savePartitionResult(partitionResult);

                for (Node node :
                        pn.getNodeSet()) {

                    PartitionDetail partitionDetail = new PartitionDetail();
                    partitionDetail.setDesc(pr.getDesc() + "的结点");
                    partitionDetail.setNodeId(node.getId());
                    partitionDetail.setPatitionResultId(pr.getId());
                    partitionDetail.setType(type);
                    partitionDetailService.savePartitionDetail(partitionDetail);
                }
            }
        }

        FileUtil.delete(workspace);

        partitionResultEdgeService.statisticsPartitionResultEdge(partitionInfo);

        PartitionInfo newPartitionInfo = new PartitionInfo();
        newPartitionInfo.setId(partitionId);
        newPartitionInfo.setStatus(1);
        partitionInfoMapper.updateByPrimaryKeySelective(newPartitionInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfPartitionResult(String dynamicInfoId, String algorithmsId, int type) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setAlgorithmsId(algorithmsId);
        partitionResult.setDynamicAnalysisInfoId(dynamicInfoId);
        partitionResult.setType(type);
        partitionResult.setFlag(1);
        return partitionResultMapper.selectCount(partitionResult);
    }

    @Override
    public int countOfPartitionResult(String id) {
        PartitionResult partitionResult = new PartitionResult();
        partitionResult.setPartitionId(id);
        partitionResult.setFlag(1);
        return partitionResultMapper.selectCount(partitionResult);
    }

    @Override
    public String getName() {
        return "partition";
    }
}
