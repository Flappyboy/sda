package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.dao.GroupRelationInfoMapper;
import cn.edu.nju.software.sda.app.dao.GroupRelationMapper;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.AppService;
import cn.edu.nju.software.sda.app.service.NodeService;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.node.NodeSet;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GroupRelationInfoDao implements InfoDao<GroupRelationInfo> {
    @Autowired
    private NodeInfoDao nodeInfoDao;
    @Autowired
    private AppService appService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private GroupRelationMapper groupRelationMapper;
    @Autowired
    private GroupRelationInfoMapper groupRelationInfoMapper;

    @Override
    public GroupRelationInfo saveProfile(GroupRelationInfo info) {
        GroupRelationInfoEntity groupRelationInfoEntity = GroupRelationInfoEntity.createNewEntity(info);
        if(groupRelationInfoEntity.getId() == null)
            groupRelationInfoEntity.setId(Sid.nextShort());
        groupRelationInfoMapper.insert(groupRelationInfoEntity);
        return info;
    }

    @Override
    public GroupRelationInfo saveDetail(GroupRelationInfo info) {
        List<GroupRelation> groupRelations = new ArrayList<>();
        List<NodeEntity> nodeEntities = nodeService.findByAppid(info.getParentId());
        Map<String, NodeEntity> nodeEntityMap = NodeEntity.toNameNodeMap(nodeEntities);
        for(GroupRelation r:info){
            r.setId(Sid.nextShort());
            NodeSet nodeSet = r.getNodeSet();
            for(Node node :nodeSet){
                NodeEntity ne = nodeEntityMap.get(node.getName());
                if(ne == null){
                    continue;
                }
                node.setId(ne.getId());
            }
            groupRelations.add(r);
        }
        groupRelationMapper.insertList(GroupRelationEntity.createNewEntities(info.getId(), groupRelations));

        GroupRelationInfoEntity grie = new GroupRelationInfoEntity();
        grie.setId(info.getId());
        grie.setStatus(1);
        groupRelationInfoMapper.updateByPrimaryKeySelective(grie);

        return info;
    }

    @Override
    public GroupRelationInfo updateProfileInfoById(GroupRelationInfo info) {
        GroupRelationInfoEntity groupRelationInfoEntity = GroupRelationInfoEntity.createNewEntity(info);
        groupRelationInfoMapper.updateByPrimaryKeySelective(groupRelationInfoEntity);
        return info;
    }

    @Override
    public GroupRelationInfo deleteById(String infoId) {
        return null;
    }

    @Override
    public GroupRelationInfo queryProfileInfoById(String infoId) {
        return  groupRelationInfoMapper.selectByPrimaryKey(infoId).toGroupRelationInfo();
    }

    @Override
    public List<GroupRelationInfo> queryProfileInfoByAppId(String appId) {
        Example example = new Example(GroupRelationInfoEntity.class);
        GroupRelationInfoEntity demo = new GroupRelationInfoEntity();
        demo.setFlag(1);
        if(StringUtils.isNotBlank(appId)){
            demo.setAppId(appId);
        }
//        if(StringUtils.isNotBlank(name)){
//            demo.setName(name);
//        }
        example.createCriteria().andEqualTo(demo);
        example.setOrderByClause("created_at desc");
        List<GroupRelationInfoEntity> groupRelationInfoEntityList = groupRelationInfoMapper.selectByExample(example);
        List<GroupRelationInfo> groupRelationInfo = new ArrayList<>();
        for (GroupRelationInfoEntity groupRelationInfoEntity :
                groupRelationInfoEntityList) {
            groupRelationInfoEntity.setAppName(appService.queryAppById(groupRelationInfoEntity.getAppId()).getName());
            groupRelationInfo.add(groupRelationInfoEntity.toGroupRelationInfo());
        }
        return groupRelationInfo;
    }

    @Override
    public List<GroupRelationInfo> queryProfileInfoByAppIdAndInfoName(String appId, String infoName) {
        Example example = new Example(GroupRelationInfoEntity.class);
        GroupRelationInfoEntity demo = new GroupRelationInfoEntity();
        demo.setFlag(1);
        if(StringUtils.isNotBlank(appId)){
            demo.setAppId(appId);
        }
        if(StringUtils.isNotBlank(infoName)){
            demo.setName(infoName);
        }
        example.createCriteria().andEqualTo(demo);
        example.setOrderByClause("created_at desc");
        List<GroupRelationInfoEntity> groupRelationInfoEntityList = groupRelationInfoMapper.selectByExample(example);
        List<GroupRelationInfo> groupRelationInfo = new ArrayList<>();
        for (GroupRelationInfoEntity groupRelationInfoEntity :
                groupRelationInfoEntityList) {
            groupRelationInfoEntity.setAppName(appService.queryAppById(groupRelationInfoEntity.getAppId()).getName());
            groupRelationInfo.add(groupRelationInfoEntity.toGroupRelationInfo());
        }
        return groupRelationInfo;
    }

    @Override
    public GroupRelationInfo queryDetailInfoById(String infoId) {
        GroupRelationInfoEntity groupRelationInfoEntity = groupRelationInfoMapper.selectByPrimaryKey(infoId);
        Example example = new Example(GroupRelationEntity.class);
        GroupRelationEntity demo = new GroupRelationEntity();
        demo.setFlag(1);
        if(StringUtils.isNotBlank(infoId)){
            demo.setInfoId(infoId);
        }
        example.createCriteria().andEqualTo(demo);
        example.setOrderByClause("created_at desc");
        List<GroupRelationEntity> groupRelationEntities = groupRelationMapper.selectByExample(example);
        GroupRelationInfo groupRelationInfo = groupRelationInfoEntity.toGroupRelationInfo();

        NodeInfo nodeInfo = nodeInfoDao.queryDetailInfoById(groupRelationInfoEntity.getAppId());
        Map<String, Node> idNodeMap = nodeInfo.getNodeSet().getIdNodeMap();

        for(GroupRelationEntity groupRelationEntity :groupRelationEntities){
            GroupRelation groupRelation = groupRelationEntity.toGroupRelation(idNodeMap);
            groupRelationInfo.addRelation(groupRelation);
        }
        return groupRelationInfo;
    }
}
