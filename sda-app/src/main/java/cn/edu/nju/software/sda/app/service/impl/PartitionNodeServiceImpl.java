package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.mock.dto.EdgeDto;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;
import cn.edu.nju.software.sda.app.mock.dto.NodeDto;
import cn.edu.nju.software.sda.app.service.*;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class PartitionNodeServiceImpl implements PartitionNodeService, SdaService {

    @Autowired
    private PartitionNodeMapper partitionNodeMapper;

    @Autowired
    private PartitionInfoMapper partitionInfoMapper;

    @Autowired
    private PairRelationService pairRelationService;

    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private PartitionNodeEdgeService partitionNodeEdgeService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private AppService appService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionNodeEntity savePartitionNode(PartitionNodeEntity partitionNodeEntity) {
        String id = Sid.nextShort();
        partitionNodeEntity.setId(id);
        partitionNodeEntity.setCreatedAt(new Date());
        partitionNodeEntity.setUpdatedAt(new Date());
        partitionNodeEntity.setFlag(1);
        partitionNodeMapper.insert(partitionNodeEntity);
        return partitionNodeEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartitionNode(PartitionNodeEntity partitionNodeEntity) {
        partitionNodeEntity.setUpdatedAt(new Date());
        partitionNodeMapper.updateByPrimaryKeySelective(partitionNodeEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePartitionNode(String partitionResultId) {
//        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
//        partitionNodeEntity.setId(partitionResultId);
//        partitionNodeEntity.setFlag(0);
//        partitionNodeEntity.setUpdatedAt(new Date());
//
//        partitionNodeMapper.updateByPrimaryKeySelective(partitionNodeEntity);
        partitionNodeMapper.deleteByPrimaryKey(partitionResultId);
    }

    @Override
    public PartitionNodeEntity queryPartitionResultById(String partitionResultId) {
        PartitionNodeEntity partitionNodeEntity = partitionNodeMapper.selectByPrimaryKey(partitionResultId);
        if(partitionNodeEntity == null || partitionNodeEntity.getFlag().equals(1))
            partitionNodeEntity = null;
        return partitionNodeEntity;
    }

    @Override
    public List<PartitionNodeEntity> queryPartitionResultListPaged(String dynamicInfoId, String algorithmsId, int type, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setFlag(1);

        Example example = new Example(PartitionNodeEntity.class);
        example.createCriteria().andEqualTo(partitionNodeEntity);
//        example.setOrderByClause("createdAt");

        List<PartitionNodeEntity> partitionNodeEntityList = partitionNodeMapper.selectByExample(example);
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
            nodeDto.setDesc(p.getDesc());
            nodeDto.setSize(count);
            graphDto.addNode(nodeDto);
        }
        List<PartitionNodeEdgeEntity> partitionNodeEdgeEntityList = partitionNodeEdgeService.findPartitionNodeEdge(partitionId);
        for (PartitionNodeEdgeEntity p : partitionNodeEdgeEntityList) {
            EdgeDto edgeDto = new EdgeDto();
            edgeDto.setId(p.getId());
            edgeDto.setCount(partitionNodeEdgeService.countOfPartitionResultEdgeCallByEdgeId(p.getId()));
            edgeDto.setSource(p.getSourceId());
            edgeDto.setTarget(p.getTargetId());
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
        List<PartitionNodeEntity> partitionNodeEntityList = partitionNodeMapper.selectByExample(example);
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
        List<PartitionNodeEntity> partitionNodeEntityList = partitionNodeMapper.selectByExample(example);

        if(partitionNodeEntityList == null || partitionNodeEntityList.size() <= 0)
            return null;
        return partitionNodeEntityList.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfPartitionResult(String dynamicInfoId, String algorithmsId, int type) {
        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setFlag(1);
        return partitionNodeMapper.selectCount(partitionNodeEntity);
    }

    @Override
    public int countOfPartitionResult(String id) {
        PartitionNodeEntity partitionNodeEntity = new PartitionNodeEntity();
        partitionNodeEntity.setPartitionId(id);
        partitionNodeEntity.setFlag(1);
        return partitionNodeMapper.selectCount(partitionNodeEntity);
    }

    @Override
    public String getName() {
        return "partition";
    }
}
