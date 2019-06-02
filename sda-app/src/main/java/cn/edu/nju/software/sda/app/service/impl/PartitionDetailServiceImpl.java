package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.PartitionDetailMapper;
import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.mock.dto.NodeDto;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.node.Node;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class PartitionDetailServiceImpl implements PartitionDetailService {
    @Autowired
    private NodeService nodeService;

    @Autowired
    private PartitionDetailMapper partitionDetailMapper;

    @Autowired
    private PartitionNodeService partitionNodeService;

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private PairRelationService pairRelationService;

    @Autowired
    private PartitionNodeEdgeService partitionNodeEdgeService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionDetailEntity savePartitionDetail(PartitionDetailEntity partitionDetailEntity) {
        String id = Sid.nextShort();
        partitionDetailEntity.setId(id);
        partitionDetailEntity.setFlag(1);
        partitionDetailEntity.setCreatedAt(new Date());
        partitionDetailEntity.setUpdatedAt(new Date());
        partitionDetailMapper.insert(partitionDetailEntity);
        return partitionDetailEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartitionDetail(PartitionDetailEntity partitionDetailEntity) {
        partitionDetailEntity.setUpdatedAt(new Date());
        partitionDetailMapper.updateByPrimaryKeySelective(partitionDetailEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePartitionDetail(String partitionDetailId) {
        PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
        partitionDetailEntity.setId(partitionDetailId);
        partitionDetailEntity.setFlag(0);
        partitionDetailMapper.updateByPrimaryKeySelective(partitionDetailEntity);
    }

    @Override
    public PartitionDetailEntity queryPartitionDetailById(String partitionDetailId) {
        PartitionDetailEntity partitionDetailEntity = partitionDetailMapper.selectByPrimaryKey(partitionDetailId);
        if (partitionDetailEntity == null || partitionDetailEntity.getFlag()!= 1)
            partitionDetailEntity = null;
        return partitionDetailEntity;
    }

    @Override
    public List<PartitionDetailEntity> queryPartitionDetailListByPartitionNodeId(String partitionNodeId) {
        PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
        partitionDetailEntity.setPartitionNodeId(partitionNodeId);
        partitionDetailEntity.setFlag(1);
        Example example = new Example(PartitionDetailEntity.class);
        example.createCriteria().andEqualTo(partitionDetailEntity);
        example.setOrderByClause("updated_at");
        return partitionDetailMapper.selectByExample(example);
    }

    @Override
    public List<HashMap<String, String>> queryPartitionDetailListPaged(String id, int type, Integer page, Integer pageSize) {
        List<HashMap<String, String>> nodes = new ArrayList<>();
        PageHelper.startPage(page, pageSize);

        PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
        partitionDetailEntity.setPartitionNodeId(id);
        partitionDetailEntity.setFlag(1);
        Example example = new Example(PartitionDetailEntity.class);
        example.createCriteria().andEqualTo(partitionDetailEntity);
        example.setOrderByClause("updated_at");
        List<PartitionDetailEntity> mylist = partitionDetailMapper.selectByExample(example);
        for (PartitionDetailEntity pd : mylist) {
            HashMap<String, String> nodemap = new HashMap<String, String>();
            if (type == 0) {
                NodeEntity node = nodeService.findById(pd.getNodeId());
                nodemap.put("nodeName", node.getName());
            }
            nodes.add(nodemap);
        }
        return nodes;
    }



//    @Override
//    public int countOfPartitionDetail(String id) {
//        PartitionDetailExample example = new PartitionDetailExample();
//        PartitionDetailExample.Criteria criteria = example.createCriteria();
//        criteria.andFlagEqualTo(1).andPatitionresultidEqualTo(id);
//        return partitionDetailMapper.countByExample(example);
//    }

    @Override
    public List<Object> queryPartitionDetailByResultId(String partitionResultId) {
        PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
        partitionDetailEntity.setPartitionNodeId(partitionResultId);
        partitionDetailEntity.setFlag(1);
        Example example = new Example(PartitionDetailEntity.class);
        example.createCriteria().andEqualTo(partitionDetailEntity);
        example.setOrderByClause("created_at");
        List<PartitionDetailEntity> mylist = partitionDetailMapper.selectByExample(example);
        List<Object> nodes = new ArrayList();
        for (PartitionDetailEntity pd : mylist) {
            Object node;
            node = nodeService.findById(pd.getNodeId());
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public PageQueryDto<NodeEntity> queryPartitionDetailByResultIdPaged(PageQueryDto<NodeEntity> dto, String partitionResultId) {
        Page p = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
        partitionDetailEntity.setPartitionNodeId(partitionResultId);
        partitionDetailEntity.setFlag(1);
        Example example = new Example(PartitionDetailEntity.class);
        example.createCriteria().andEqualTo(partitionDetailEntity);
        example.setOrderByClause("updated_at");
        List<PartitionDetailEntity> mylist = partitionDetailMapper.selectByExample(example);
        List<NodeEntity> nodes = new ArrayList();
        for (PartitionDetailEntity pd : mylist) {
            NodeEntity node;
                node = nodeService.findById(pd.getNodeId());
            nodes.add(node);
        }

        return dto.addResult(nodes, p.getTotal());
    }

    @Override
    public int countOfPartitionDetailByResultId(String partitionResultId) {
        PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
        partitionDetailEntity.setFlag(1);
        partitionDetailEntity.setPartitionNodeId(partitionResultId);
        return partitionDetailMapper.selectCount(partitionDetailEntity);
    }

    @Override
    public int countOfPartitionDetail(String partitionResultId, int type) {
        PartitionDetailEntity partitionDetailEntity = new PartitionDetailEntity();
        partitionDetailEntity.setFlag(1);
        partitionDetailEntity.setPartitionNodeId(partitionResultId);
        return partitionDetailMapper.selectCount(partitionDetailEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraphOperateDto moveNodeToPartition(String nodeId, PartitionNodeEntity oldPartitionNodeEntity, PartitionNodeEntity targetPartitionNodeEntity) {
        PartitionGraphOperateDto.Builder builder = new PartitionGraphOperateDto.Builder();

        /**
         * 简单的方案：调整节点所属划分，重新计算边的关系
         */
        PartitionDetailEntity pdDemo = new PartitionDetailEntity();
        pdDemo.setNodeId(nodeId);
        pdDemo.setPartitionNodeId(oldPartitionNodeEntity.getId());
        Example example = new Example(PartitionDetailEntity.class);
        example.createCriteria().andEqualTo(pdDemo);

        PartitionDetailEntity pd = new PartitionDetailEntity();
        pd.setPartitionNodeId(targetPartitionNodeEntity.getId());
        partitionDetailMapper.updateByExampleSelective(pd, example);

        partitionNodeEdgeService.statisticsPartitionResultEdge(oldPartitionNodeEntity.getPartitionId());
        builder.graphReload();

        /**
         * 复杂的方案：先从划分中删除节点，再向划分中添加节点
         */
//        builder.attachOperate(removeNodeFromPartition(nodeId, oldPartitionNodeEntity));
//        builder.attachOperate(addNodeToPartition(nodeId, targetPartitionNodeEntity));
        return builder.build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraphOperateDto removeNodeFromPartition(String nodeId, PartitionNodeEntity oldPartitionNodeEntity) {
        PartitionGraphOperateDto.Builder builder = new PartitionGraphOperateDto.Builder();

        /*PartitionDetailEntity pdDemo = new PartitionDetailEntity();
        pdDemo.setNodeId(nodeId);
        pdDemo.setPartitionNodeId(oldPartitionNodeEntity.getId());
        Example example = new Example(PartitionDetailEntity.class);
        example.createCriteria().andEqualTo(pdDemo);
        PartitionDetailEntity pd = new PartitionDetailEntity();
        pd.setPartitionNodeId("");
        partitionDetailMapper.updateByExampleSelective(pd, example);*/

        //修改原划分之间的边的关系
       /* List<PairRelationEntity> pairRelationEntityList = pairRelationService.pairRelationsForNode(nodeId, Arrays.asList(oldPartitionNodeEntity.getDynamicAnalysisInfoId()));
        Set<PairRelationEntity> pairRelationEntitySet = new HashSet<>(pairRelationEntityList);
        List<PartitionNodeEdgeEntity> oldPartitionNodeEdgeEntityList = partitionNodeEdgeService.findPartitionResultEdgeByNode(oldPartitionNodeEntity.getId(),nodeId);
        partitionNodeEdgeService.fillPartitionResultEdgeCall(oldPartitionNodeEdgeEntityList);
        for (PartitionNodeEdgeEntity pre :
                oldPartitionNodeEdgeEntityList) {
            List<PartitionNodeEdgePairEntity> partitionNodeEdgePairEntityList = pre.getPartitionNodeEdgePairEntities();
            for (PartitionNodeEdgePairEntity prec :
                    partitionNodeEdgePairEntityList) {
                if(prec.getCallId() != null && pairRelationEntitySet.contains(prec.getCallId())){
                    partitionNodeEdgeService.removeEdgeCall(prec.getCallId());
                }
            }
        }*/

        //删除为调用为0的边

        //删除没有node的partitionResult

        return builder.build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraphOperateDto addNodeToPartition(String nodeId, PartitionNodeEntity targetPartitionNodeEntity) {
       /* PartitionDetailEntity pdDemo = new PartitionDetailEntity();
        pdDemo.setNodeId(nodeId);
        pdDemo.setPartitionNodeId(oldPartitionResult.getId());
        Example example = new Example(PartitionDetailEntity.class);
        example.createCriteria().andEqualTo(pdDemo);
        PartitionDetailEntity pd = new PartitionDetailEntity();
        pd.setPartitionNodeId("");
        partitionDetailMapper.updateByExampleSelective(pd, example);*/

        //在新的划分中添加边的关系
        List<PartitionNodeEdgeEntity> targetPartitionNodeEdgeEntityList = partitionNodeEdgeService.findPartitionResultEdgeByPartitionResult(targetPartitionNodeEntity.getPartitionId(), targetPartitionNodeEntity.getId());

        return null;
    }
}
