package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.PartitionDetailMapper;
import cn.edu.nju.software.sda.app.dto.PartitionGraphOperateDto;
import cn.edu.nju.software.sda.app.entity.*;
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
public class PartitionDetailServiceImpl implements PartitionDetailService {
    @Autowired
    private Sid sid;
    @Autowired
    private ClassNodeService classNodeService;
    @Autowired
    private MethodNodeService methodNodeService;
    @Autowired
    private PartitionDetailMapper partitionDetailMapper;
    @Autowired
    private PartitionResultService partitionResultService;

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private CallService callService;

    @Autowired
    private PartitionResultEdgeService partitionResultEdgeService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionDetail savePartitionDetail(PartitionDetail partitionDetail) {
        String id = sid.nextShort();
        partitionDetail.setId(id);
        partitionDetail.setCreatedAt(new Date());
        partitionDetail.setUpdatedAt(new Date());
        partitionDetail.setFlag(1);
        partitionDetailMapper.insert(partitionDetail);
        return partitionDetail;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePartitionDetail(PartitionDetail partitionDetail) {
        partitionDetail.setUpdatedAt(new Date());
        partitionDetailMapper.updateByPrimaryKeySelective(partitionDetail);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePartitionDetail(String partitionDetailId) {
        PartitionDetail partitionDetail = new PartitionDetail();
        partitionDetail.setId(partitionDetailId);
        partitionDetail.setFlag(0);
        partitionDetail.setUpdatedAt(new Date());
        partitionDetailMapper.updateByPrimaryKeySelective(partitionDetail);
    }

    @Override
    public PartitionDetail queryPartitionDetailById(String partitionDetailId) {
        PartitionDetail partitionDetail = partitionDetailMapper.selectByPrimaryKey(partitionDetailId);
        if (partitionDetail == null || partitionDetail.getFlag()!= 1)
            partitionDetail = null;
        return partitionDetail;
    }

    @Override
    public List<HashMap<String, String>> queryPartitionDetailListPaged(String id, int type, Integer page, Integer pageSize) {
        List<HashMap<String, String>> nodes = new ArrayList<>();
        PageHelper.startPage(page, pageSize);

        PartitionDetail partitionDetail = new PartitionDetail();
        partitionDetail.setPatitionResultId(id);
        partitionDetail.setFlag(1);
        partitionDetail.setType(type);
        Example example = new Example(PartitionDetail.class);
        example.createCriteria().andEqualTo(partitionDetail);
        example.setOrderByClause("created_at");
        List<PartitionDetail> mylist = partitionDetailMapper.selectByExample(example);
        for (PartitionDetail pd : mylist) {
            HashMap<String, String> nodemap = new HashMap<String, String>();
            if (type == 0) {
                ClassNode node = classNodeService.findById(pd.getNodeId());
                nodemap.put("nodeName", node.getName());
            }
            if (type == 1) {
                MethodNode node = methodNodeService.findById(pd.getNodeId());
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
        PartitionDetail partitionDetail = new PartitionDetail();
        partitionDetail.setPatitionResultId(partitionResultId);
        partitionDetail.setFlag(1);
        Example example = new Example(PartitionDetail.class);
        example.createCriteria().andEqualTo(partitionDetail);
        example.setOrderByClause("created_at");
        List<PartitionDetail> mylist = partitionDetailMapper.selectByExample(example);
        List<Object> nodes = new ArrayList();
        for (PartitionDetail pd : mylist) {
            Object node;
            int type = pd.getType();
            if (type == 0) {
                node = classNodeService.findById(pd.getNodeId());
            }else{
                node = methodNodeService.findById(pd.getNodeId());
            }
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public List<Object> queryPartitionDetailByResultIdPaged(String partitionResultId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        PartitionDetail partitionDetail = new PartitionDetail();
        partitionDetail.setPatitionResultId(partitionResultId);
        partitionDetail.setFlag(1);
        Example example = new Example(PartitionDetail.class);
        example.createCriteria().andEqualTo(partitionDetail);
        example.setOrderByClause("created_at");
        List<PartitionDetail> mylist = partitionDetailMapper.selectByExample(example);
        List<Object> nodes = new ArrayList();
        for (PartitionDetail pd : mylist) {
            Object node;
            int type = pd.getType();
            if (type == 0) {
                node = classNodeService.findById(pd.getNodeId());
            }else{
                node = methodNodeService.findById(pd.getNodeId());
            }
            nodes.add(node);
        }
        return nodes;
    }



    @Override
    public int countOfPartitionDetailByResultId(String partitionResultId) {
        PartitionDetail partitionDetail = new PartitionDetail();
        partitionDetail.setFlag(1);
        partitionDetail.setPatitionResultId(partitionResultId);
        return partitionDetailMapper.selectCount(partitionDetail);
    }

    @Override
    public int countOfPartitionDetail(String partitionResultId, int type) {
        PartitionDetail partitionDetail = new PartitionDetail();
        partitionDetail.setFlag(1);
        partitionDetail.setPatitionResultId(partitionResultId);
        partitionDetail.setType(type);
        return partitionDetailMapper.selectCount(partitionDetail);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraphOperateDto moveNodeToPartition(String nodeId, PartitionResult oldPartitionResult, PartitionResult targetPartitionResult) {
        PartitionGraphOperateDto.Builder builder = new PartitionGraphOperateDto.Builder();

        /**
         * 简单的方案：调整节点所属划分，重新计算边的关系
         */
        PartitionDetail pdDemo = new PartitionDetail();
        pdDemo.setNodeId(nodeId);
        pdDemo.setPatitionResultId(oldPartitionResult.getId());
        Example example = new Example(PartitionDetail.class);
        example.createCriteria().andEqualTo(pdDemo);

        PartitionDetail pd = new PartitionDetail();
        pd.setPatitionResultId(targetPartitionResult.getId());
        partitionDetailMapper.updateByExampleSelective(pd, example);

        partitionResultEdgeService.statisticsPartitionResultEdge(partitionService.findPartitionById(oldPartitionResult.getPartitionId()));
        builder.graphReload();

        /**
         * 复杂的方案：先从划分中删除节点，再向划分中添加节点
         */
//        builder.attachOperate(removeNodeFromPartition(nodeId, oldPartitionResult));
//        builder.attachOperate(addNodeToPartition(nodeId, targetPartitionResult));
        return builder.build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraphOperateDto removeNodeFromPartition(String nodeId, PartitionResult oldPartitionResult) {
        PartitionGraphOperateDto.Builder builder = new PartitionGraphOperateDto.Builder();

        /*PartitionDetail pdDemo = new PartitionDetail();
        pdDemo.setNodeId(nodeId);
        pdDemo.setPatitionResultId(oldPartitionResult.getId());
        Example example = new Example(PartitionDetail.class);
        example.createCriteria().andEqualTo(pdDemo);
        PartitionDetail pd = new PartitionDetail();
        pd.setPatitionResultId("");
        partitionDetailMapper.updateByExampleSelective(pd, example);*/

        //修改原划分之间的边的关系
        List<CallInfo> callInfoList = callService.findCallInfo(nodeId, oldPartitionResult.getDynamicAnalysisInfoId());
        Set<CallInfo> callInfoSet = new HashSet<>(callInfoList);
        List<PartitionResultEdge> oldPartitionResultEdgeList = partitionResultEdgeService.findPartitionResultEdgeByNode(oldPartitionResult.getId(),nodeId);
        partitionResultEdgeService.fillPartitionResultEdgeCall(oldPartitionResultEdgeList);
        for (PartitionResultEdge pre :
                oldPartitionResultEdgeList) {
            List<PartitionResultEdgeCall> partitionResultEdgeCallList = pre.getPartitionResultEdgeCallList();
            for (PartitionResultEdgeCall prec :
                    partitionResultEdgeCallList) {
                if(prec.getCallid() != null && callInfoSet.contains(prec.getCallid())){
                    partitionResultEdgeService.removeEdgeCall(prec.getCallid());
                }
            }
        }

        //删除为调用为0的边

        //删除没有node的partitionResult

        return builder.build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraphOperateDto addNodeToPartition(String nodeId, PartitionResult targetPartitionResult) {
       /* PartitionDetail pdDemo = new PartitionDetail();
        pdDemo.setNodeId(nodeId);
        pdDemo.setPatitionResultId(oldPartitionResult.getId());
        Example example = new Example(PartitionDetail.class);
        example.createCriteria().andEqualTo(pdDemo);
        PartitionDetail pd = new PartitionDetail();
        pd.setPatitionResultId("");
        partitionDetailMapper.updateByExampleSelective(pd, example);*/

        //在新的划分中添加边的关系
        List<PartitionResultEdge> targetPartitionResultEdgeList = partitionResultEdgeService.findPartitionResultEdgeByPartitionResult(targetPartitionResult.getPartitionId(), targetPartitionResult.getId());

        return null;
    }
}
