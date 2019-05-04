package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.entity.bean.PartitionEdge;
import cn.edu.nju.software.sda.app.entity.bean.PartitionGraph;
import cn.edu.nju.software.sda.app.entity.bean.PartitionNode;
import cn.edu.nju.software.sda.app.entity.bean.PartitionNodeEdge;
import cn.edu.nju.software.sda.app.service.DynamicCallService;
import cn.edu.nju.software.sda.app.service.PartitionResultService;
import cn.edu.nju.software.sda.app.service.PartitionService;
import cn.edu.nju.software.sda.app.service.StaticCallService;
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
public class PartitionServiceImpl implements PartitionService {
    @Autowired
    private PartitionInfoMapper partitionMapper;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private AlgorithmsMapper algorithmsMapper;

    @Autowired
    private PartitionResultMapper partitionResultMapper;

    @Autowired
    private StaticCallInfoMapper staticCallInfoMapper;

    @Autowired
    private StaticCallService staticCallService;

    @Autowired
    private DynamicCallInfoMapper dynamicCallInfoMapper;

    @Autowired
    private DynamicCallService dynamicCallService;

    @Autowired
    private PartitionDetailMapper partitionDetailMapper;

    @Autowired
    private ClassNodeMapper classNodeMapper;
    @Autowired
    private MethodNodeMapper methodNodeMapper;

    @Autowired
    private PartitionResultService partitionResultService;

    @Autowired
    private Sid sid;

    @Override
    public PartitionInfo findPartitionById(String partitionId) {
        return partitionMapper.selectByPrimaryKey(partitionId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void addPartition(PartitionInfo partition) {
        String id = sid.nextShort();
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
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delPartition(String id) {
        PartitionInfo partition = new PartitionInfo();
        partition.setId(id);
        partition.setFlag(0);
        updatePartition(partition);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updatePartition(PartitionInfo partition) {
        partition.setUpdatedat(new Date());
        partitionMapper.updateByPrimaryKeySelective(partition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<HashMap<String, Object>> findBycondition(Integer page, Integer pageSize, String appName, String desc, String algorithmsid, Integer type) {
        // 开始分页
        PageHelper.startPage(page, pageSize);

        PartitionInfoExample example = new PartitionInfoExample();
        PartitionInfoExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);
        if (appName != null && appName != "" && !appName.isEmpty()) {
            AppExample appexample = new AppExample();
            AppExample.Criteria appcriteria = appexample.createCriteria();
            appcriteria.andFlagEqualTo(1).andNameEqualTo(appName);
            List<App> apps = appMapper.selectByExample(appexample);
            List<String> appIds = new ArrayList<>();
            for (App app : apps) {
                appIds.add(app.getId());
            }
            criteria.andAppidIn(appIds);
        }
        if (desc != null && desc != "" && !desc.isEmpty()) {
            criteria.andDescEqualTo(desc);
        }
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
            App app = appMapper.selectByPrimaryKey(appid);
            String algoid = partitionInfo.getAlgorithmsid();
            Algorithms a = algorithmsMapper.selectByPrimaryKey(algoid);
            result.put("id", partitionInfo.getId());
            if (app != null)
                result.put("appName", app.getName());
            result.put("dynamicAnalysisInfoId", partitionInfo.getDynamicanalysisinfoid());
            if (a != null)
                result.put("algorithmsName", a.getName());
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
    public int count(String appName, String desc, String algorithmsid, Integer type) {
        PartitionInfoExample example = new PartitionInfoExample();
        PartitionInfoExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);
        if (appName != null && appName != "" && !appName.isEmpty()) {
            AppExample appexample = new AppExample();
            AppExample.Criteria appcriteria = appexample.createCriteria();
            appcriteria.andFlagEqualTo(1).andNameEqualTo(appName);
            List<App> apps = appMapper.selectByExample(appexample);
            List<String> appIds = new ArrayList<>();
            for (App app : apps) {
                appIds.add(app.getId());
            }
            criteria.andAppidIn(appIds);
        }
        if (desc != null && desc != "" && !desc.isEmpty()) {
            criteria.andDescEqualTo(desc);
        }
        if (algorithmsid != "" && algorithmsid != null && !algorithmsid.isEmpty())
            criteria.andAlgorithmsidEqualTo(algorithmsid);
        if (type != null)
            criteria.andTypeEqualTo(type);
        int count = partitionMapper.countByExample(example);
        return count;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartitionGraph getGraph(String partitionInfoId) {
        PartitionGraph partitionGraph = new PartitionGraph();

        //找出所有社区
        PartitionInfo partitionInfo = partitionMapper.selectByPrimaryKey(partitionInfoId);
        if(partitionInfo == null)
            return  null;
        int type = partitionInfo.getType();
        String appid = partitionInfo.getAppid();
        String namicanalysisinfoid = partitionInfo.getDynamicanalysisinfoid();


        Example example = new Example(PartitionResult.class);
        PartitionResult partitionResultExample = new PartitionResult();
        partitionResultExample.setPartitionId(partitionInfoId);
        partitionResultExample.setFlag(1);
        example.createCriteria().andEqualTo(partitionResultExample);
        List<PartitionResult> partitionResultList = partitionResultMapper.selectByExample(example);

        HashMap<String, List<String>> nodes = new HashMap<>();

        //社区节点
        List<PartitionNode> partitionNodes = new ArrayList<>();
        for (PartitionResult partitionResult : partitionResultList) {
            List<String> nodeIds = new ArrayList<>();
            PartitionNode partitionNode = new PartitionNode();
            partitionNode.setCommunity(partitionResult);
            List<ClassNode> classNodes = new ArrayList<>();
            List<MethodNode> methodNodes = new ArrayList<>();

            PartitionDetail pd = new PartitionDetail();
            pd.setPatitionResultId(partitionResult.getId());
            pd.setFlag(1);
            Example pdExample = new Example(PartitionDetail.class);
            example.createCriteria().andEqualTo(pd);

            List<PartitionDetail> partitionDetails = partitionDetailMapper.selectByExample(pdExample);
            for (PartitionDetail partitionDetail : partitionDetails) {
                if (type == 0) {
                    ClassNode classNode = classNodeMapper.selectByPrimaryKey(partitionDetail.getNodeId());
                    classNodes.add(classNode);
                    nodeIds.add(classNode.getId());
                }
                if (type == 1) {
                    MethodNode methodNode = methodNodeMapper.selectByPrimaryKey(partitionDetail.getNodeId());
                    methodNodes.add(methodNode);
                    nodeIds.add(methodNode.getId());
                }
            }
            nodes.put(partitionResult.getId(), nodeIds);
            partitionNode.setClassNodes(classNodes);
            partitionNode.setMethodNodes(methodNodes);
            partitionNode.setClaaSize(classNodes.size());
            partitionNode.setMethodSize(methodNodes.size());
            partitionNodes.add(partitionNode);
        }

        //社区边
        List<PartitionEdge> partitionEdges = new ArrayList<>();
        for (PartitionNode partitionNode1 : partitionNodes) {
            for (PartitionNode partitionNode2 : partitionNodes) {
                String communityId1 = partitionNode1.getCommunity().getId();
                String communityId2 = partitionNode2.getCommunity().getId();
                if (communityId1 != communityId2) {
                    //查询静态的边
                    List<StaticCallInfo> staticCallInfos = new ArrayList<>();
                    staticCallInfos = getStaticEdge(nodes.get(communityId1),nodes.get(communityId2),type,appid);
                    staticCallInfos.addAll(getStaticEdge(nodes.get(communityId2),nodes.get(communityId1),type,appid));
                    //查询动态的边
                    List<DynamicCallInfo> dynamicCallInfos = new ArrayList<>();
                    dynamicCallInfos = getDynamicEdge(nodes.get(communityId1),nodes.get(communityId2),type,namicanalysisinfoid);
                    dynamicCallInfos.addAll(getDynamicEdge(nodes.get(communityId2),nodes.get(communityId1),type,namicanalysisinfoid));
                    //合并边
                    List<PartitionNodeEdge>  partitionNodeEdges = getMergedEdge(staticCallInfos,dynamicCallInfos,type);
                    //判断两社区之间有边
                    if(partitionNodeEdges!=null&&partitionNodeEdges.size()>0){
                        PartitionEdge partitionEdge = new PartitionEdge();
                        partitionEdge.setSourceCommunityId(communityId1);
                        partitionEdge.setTargetCommunityId(communityId2);
                        partitionEdge.setEdges(partitionNodeEdges);
                        partitionEdges.add(partitionEdge);
                    }
                }
            }
        }

        partitionGraph.setPartitionNodes(partitionNodes);
        partitionGraph.setPartitionEdges(partitionEdges);
        return partitionGraph;
    }

    //查询静态的边
    public List<StaticCallInfo> getStaticEdge(List<String> callerNodeId1, List<String> calleeNodeId2, int type, String appid) {
        StaticCallInfo sciDemo = new StaticCallInfo();
        sciDemo.setType(type);
        sciDemo.setFlag(1);
        sciDemo.setAppId(appid);
        Example example = new Example(StaticCallInfo.class);
        example.createCriteria().andEqualTo(sciDemo).andIn("caller", callerNodeId1).andIn("callee", calleeNodeId2);
        List<StaticCallInfo> staticCallInfos = staticCallInfoMapper.selectByExample(example);
        return staticCallInfos;
    }

    //查询动态的边
    public List<DynamicCallInfo> getDynamicEdge(List<String> callerNodeId1, List<String> calleeNodeId2, int type, String dynamicanalysisinfoid) {
        DynamicCallInfo dciDemo = new DynamicCallInfo();
        dciDemo.setType(type);
        dciDemo.setFlag(1);
        dciDemo.setDynamicAnalysisInfoId(dynamicanalysisinfoid);
        Example example = new Example(DynamicCallInfo.class);
        example.createCriteria().andEqualTo(dciDemo).andIn("caller", callerNodeId1).andIn("callee", calleeNodeId2);
        List<DynamicCallInfo> dynamicCallInfos = dynamicCallInfoMapper.selectByExample(example);
        return dynamicCallInfos;
    }

    //合并边
    public List<PartitionNodeEdge> getMergedEdge(List<StaticCallInfo> staticEdges,List<DynamicCallInfo> dynamicEdges,int type){
        List<PartitionNodeEdge> partitionNodeEdges = new ArrayList<>();
        HashMap<String ,PartitionNodeEdge> edgesMap = new HashMap<>();
        for(StaticCallInfo staticEdge:staticEdges){
            String key = staticEdge.getCaller()+"_"+staticEdge.getCallee();
            PartitionNodeEdge partitionNodeEdge = createPartitionNodeEdge(staticEdge.getCaller(),staticEdge.getCallee(),type,staticEdge.getCount());
            edgesMap.put(key,partitionNodeEdge);
        }
        for(DynamicCallInfo dynamicEdge:dynamicEdges){
            String key = dynamicEdge.getCaller()+"_"+dynamicEdge.getCallee();
            PartitionNodeEdge partitionNodeEdge = edgesMap.get(key);
            if(partitionNodeEdge == null){
                partitionNodeEdge = createPartitionNodeEdge(dynamicEdge.getCaller(),dynamicEdge.getCallee(),type,dynamicEdge.getCount());
            }else{
                int count = partitionNodeEdge.getCount()+dynamicEdge.getCount();
                partitionNodeEdge.setCount(count);
            }
            edgesMap.put(key,partitionNodeEdge);
        }
        for (Map.Entry<String ,PartitionNodeEdge> entry : edgesMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            partitionNodeEdges.add(entry.getValue());
        }
        return partitionNodeEdges;
    }

    //根据节点id创建划分节点边
    public PartitionNodeEdge createPartitionNodeEdge(String callerId,String calleeId,int type,int count){
        PartitionNodeEdge partitionNodeEdge = new PartitionNodeEdge();
        if (type == 0) {
            ClassNode callerClassNode = classNodeMapper.selectByPrimaryKey(callerId);
            ClassNode calleeClassNode = classNodeMapper.selectByPrimaryKey(calleeId);
            partitionNodeEdge.setCaller(callerClassNode.getName());
            partitionNodeEdge.setCallerName(callerClassNode.getName());
            partitionNodeEdge.setCallerFullName(callerClassNode.getName());
            partitionNodeEdge.setCallee(calleeClassNode.getId());
            partitionNodeEdge.setCalleeName(calleeClassNode.getName());
            partitionNodeEdge.setCalleeFullName(calleeClassNode.getName());
            partitionNodeEdge.setCount(count);
        }
        if (type == 1) {
            MethodNode callerMethodNode = methodNodeMapper.selectByPrimaryKey(callerId);
            MethodNode calleeMethodNode = methodNodeMapper.selectByPrimaryKey(calleeId);
            partitionNodeEdge.setCaller(callerMethodNode.getId());
            partitionNodeEdge.setCallerName(callerMethodNode.getName());
            partitionNodeEdge.setCallerFullName(callerMethodNode.getFullname());
            partitionNodeEdge.setCallee(calleeMethodNode.getId());
            partitionNodeEdge.setCalleeName(calleeMethodNode.getName());
            partitionNodeEdge.setCalleeFullName(calleeMethodNode.getFullname());
            partitionNodeEdge.setCount(count);
        }
        return partitionNodeEdge;
    }
}
