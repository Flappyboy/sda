package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.AppMapper;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.app.utils.SqlUtil;
import cn.edu.nju.software.sda.core.domain.App;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import cn.edu.nju.software.sda.core.domain.info.RelationInfo;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private PairRelationService pairRelationService;

    @Autowired
    private PartitionService partitionService;

    @Autowired
    private PartitionNodeService partitionNodeService;

    @Autowired
    private PartitionDetailService partitionDetailService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppEntity saveApp(AppEntity app) {
        String id = Sid.nextShort();
        app.setId(id);
        app.setCreatedAt(new Date());
        app.setUpdatedAt(new Date());
        app.setFlag(1);
        appMapper.insert(app);

        /*Thread thread = new Thread(new Runnable() {
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
*/
        return app;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AppEntity updateApp(AppEntity app) {
        app.setUpdatedAt(new Date());
        appMapper.updateByPrimaryKeySelective(app);
        return queryAppById(app.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteApp(String appId) {
        AppEntity app = new AppEntity();
        app.setId(appId);
        app.setFlag(0);
        app.setUpdatedAt(new Date());
        appMapper.updateByPrimaryKeySelective(app);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteApps(List<String> appIds) {
        for (String id:
                appIds) {
            deleteApp(id);
        }
    }

    @Override
    public List<AppEntity> queryApps(AppEntity app) {
        return appMapper.select(app);
    }

    @Override
    public AppEntity queryAppById(String appId) {
        AppEntity app = appMapper.selectByPrimaryKey(appId);
        return app;
    }

    @Override
    public List<AppEntity> queryAppListPaged(Integer page, Integer pageSize, AppEntity app) {
        // 开始分页
        PageHelper.startPage(page, pageSize);

        Example example = new Example(AppEntity.class);
        example.createCriteria().andEqualTo(app);
        example.setOrderByClause("created_at desc");

        List<AppEntity> appList = appMapper.selectByExample(example);
        return appList;
    }

    @Override
    public PageQueryDto<AppEntity> queryAppListByLikePaged(@NotNull PageQueryDto<AppEntity> dto, AppEntity app) {
        // 开始分页
        Page p = PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), true);

        Example example = new Example(AppEntity.class);
        if(app != null) {
            example.createCriteria()
                    .andEqualTo("flag", 1)
                    .andLike("name", SqlUtil.like(app.getName()))
                    .andLike("desc", SqlUtil.like(app.getDesc()));
        }
        example.setOrderByClause("created_at desc");
        List<AppEntity> appList = appMapper.selectByExample(example);
        return dto.addResult(appList, p.getTotal());
    }

    @Override
    public App getAppWithInfo(String appId, List<String> infoIdList) {
        App app = new App();
        app.setId(appId);

        NodeSet nodeSet = new NodeSet();
        app.setNodeSet(nodeSet);

        InfoSet infoSet = new InfoSet();
        app.setInfoSet(infoSet);

        List<NodeEntity> nodeEntityList = nodeService.findByAppid(appId);

        for(NodeEntity nodeEntity : nodeEntityList){
            nodeSet.addNode(new ClassNode(nodeEntity.getName()));
        }

        RelationInfo info = null;

        if (infoIdList != null && infoIdList.size() > 0) {
            info = new PairRelationInfo(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT);
            infoSet.addInfo(info);
            List<PairRelationEntity> dynamicCallInfos = pairRelationService.findByDynamicAndType(infoIdList.get(0), 0);
            for (PairRelationEntity callInfo : dynamicCallInfos) {
                PairRelation pairRelation = new PairRelation(callInfo.getId(), callInfo.getValue().doubleValue(), nodeSet.getNodeById(callInfo.getSourceNode()),
                        nodeSet.getNodeById(callInfo.getTargetNode()));
                info.addRelationByAddValue(pairRelation);
            }
        }
        return app;
    }
}
