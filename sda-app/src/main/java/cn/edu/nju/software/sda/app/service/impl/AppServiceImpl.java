package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.AppMapper;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.entity.adapter.AppAdapter;
import cn.edu.nju.software.sda.app.entity.adapter.ClassNodeAdapter;
import cn.edu.nju.software.sda.app.mock.dto.GraphDto;
import cn.edu.nju.software.sda.app.mock.dto.NodeDto;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.entity.info.InfoSet;
import cn.edu.nju.software.sda.core.entity.info.PairRelation;
import cn.edu.nju.software.sda.core.entity.info.RelationInfo;
import cn.edu.nju.software.sda.core.entity.node.Node;
import cn.edu.nju.software.sda.core.entity.node.NodeSet;
import cn.edu.nju.software.sda.core.entity.partition.Partition;
import cn.edu.nju.software.sda.core.entity.partition.PartitionNode;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;

    @Autowired
    private StaticCallService staticCallService;

    @Autowired
    private ClassNodeService classNodeService;

    @Autowired
    private DynamicCallService dynamicCallService;

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private PartitionResultService partitionResultService;

    @Autowired
    private PartitionDetailService partitionDetailService;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public App saveApp(App app) {
        String id = sid.nextShort();
        app.setId(id);
        app.setCreatedat(new Date());
        app.setUpdatedat(new Date());
        app.setFlag(1);
        appMapper.insert(app);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(app);
                    staticCallService.saveStaticAnalysis(app, 1);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        });
        thread.start();

        return app;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateApp(App app) {
        app.setUpdatedat(new Date());
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(app.getId()).andFlagEqualTo(1);
        appMapper.updateByExampleSelective(app, example);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteApp(String appId) {
        App app = new App();
        app.setId(appId);
        app.setFlag(0);
        app.setUpdatedat(new Date());
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(appId).andFlagEqualTo(1);
        appMapper.updateByExampleSelective(app, example);
    }

    @Override
    public App queryAppById(String appId) {
        App app = null;
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(appId);
        List<App> apps = appMapper.selectByExample(example);
        if (apps.size() > 0 && apps != null)
            app = apps.get(0);
        return app;
    }

    @Override
    public List<App> queryUserListPaged(Integer page, Integer pageSize,String appName,String desc) {
        // 开始分页
        PageHelper.startPage(page, pageSize);

        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);
        if(appName!=""&&appName!=null&&!appName.isEmpty())
            criteria.andNameEqualTo(appName);
        if(desc!=""&&desc!=null&&!desc.isEmpty())
            criteria.andDescLike(desc);
        example.setOrderByClause("createdAt desc");
        List<App> appList = appMapper.selectByExample(example);
        return appList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfApp(String appName,String desc) {
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        if(appName!=""&&appName!=null&&!appName.isEmpty())
            criteria.andNameEqualTo(appName);
        if(desc!=""&&desc!=null&&!desc.isEmpty())
            criteria.andDescLike(desc);
        criteria.andFlagEqualTo(1);
        int count =appMapper.countByExample(example);
        return count;
    }

    @Override
    public AppAdapter getAppWithInfo(String appId, List<String> infoIdList) {
        AppAdapter app = new AppAdapter();
        app.setId(appId);

        NodeSet<ClassNodeAdapter> nodeSet = new NodeSet();
        app.setNodeSet(nodeSet);

        InfoSet infoSet = new InfoSet();
        app.setInfoSet(infoSet);

        List<ClassNode> classNodeList = classNodeService.findByAppid(appId);

        for(ClassNode classNode: classNodeList){
            nodeSet.addNode(new ClassNodeAdapter(classNode, cn.edu.nju.software.sda.core.entity.node.ClassNode.ClassNodeType.NORMAL));
        }

        RelationInfo info = new RelationInfo(PairRelation.STATIC_CALL_COUNT, ClassNodeAdapter.clazz, PairRelation.class);
        infoSet.addInfo(info);

        List<StaticCallInfo> staticCallInfos = staticCallService.findByAppIdAndType(appId, 0);
        for (StaticCallInfo callInfo : staticCallInfos) {
            PairRelation pairRelation = new PairRelation(callInfo.getId(), callInfo.getCount().doubleValue(),
                    ClassNodeAdapter.clazz, nodeSet.getNodeById(callInfo.getCaller()),
                    nodeSet.getNodeById(callInfo.getCallee()));
            info.addRelationByAddValue(pairRelation);
        }

        if (infoIdList != null && infoIdList.size() > 0) {
            info = new RelationInfo(PairRelation.DYNAMIC_CALL_COUNT, ClassNodeAdapter.clazz, PairRelation.class);
            infoSet.addInfo(info);
            List<DynamicCallInfo> dynamicCallInfos = dynamicCallService.findByDynamicAndType(infoIdList.get(0), 0);
            for (DynamicCallInfo callInfo : dynamicCallInfos) {
                PairRelation pairRelation = new PairRelation(callInfo.getId(), callInfo.getCount().doubleValue(),
                        ClassNodeAdapter.clazz, nodeSet.getNodeById(callInfo.getCaller()),
                        nodeSet.getNodeById(callInfo.getCallee()));
                info.addRelationByAddValue(pairRelation);
            }
        }
        return app;
    }

    @Override
    public AppAdapter getAppWithPartition(String partitionId) {
        PartitionInfo partitionInfo = partitionService.findPartitionById(partitionId);
        String appId = partitionInfo.getAppid();
        List<String> infoIdList = new ArrayList<>();
        infoIdList.add(partitionInfo.getDynamicanalysisinfoid());
        AppAdapter app = getAppWithInfo(appId, infoIdList);

        Partition partition = new Partition();
        partition.setId(partitionId);

        Set<PartitionNode> partitionNodeSet = new HashSet<>();

        List<PartitionResult> partitionResults = partitionResultService.queryPartitionResult(partitionId);
        for (PartitionResult partitionResult :
                partitionResults ) {
            PartitionNode partitionNode = new PartitionNode(partitionResult.getName());
            partitionNodeSet.add(partitionNode);
            List<Object> list = partitionDetailService.queryPartitionDetailByResultId(partitionResult.getId());
            for(Object o: list){
                if(o instanceof ClassNode){
                    ClassNode classNode = (ClassNode) o;
                    Node node  = new ClassNodeAdapter(classNode, cn.edu.nju.software.sda.core.entity.node.ClassNode.ClassNodeType.NORMAL);
                    partitionNode.addNode(node);
                }
            }
        }

        partition.setPartitionNodeSet(partitionNodeSet);
        app.setPartition(partition);
        return app;
    }
}
