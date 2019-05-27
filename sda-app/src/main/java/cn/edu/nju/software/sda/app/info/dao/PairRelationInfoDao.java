package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.dao.PairRelationInfoMapper;
import cn.edu.nju.software.sda.app.dao.PairRelationMapper;
import cn.edu.nju.software.sda.app.entity.NodeEntity;
import cn.edu.nju.software.sda.app.entity.PairRelationEntity;
import cn.edu.nju.software.sda.app.entity.PairRelationInfoEntity;
import cn.edu.nju.software.sda.app.service.NodeService;
import cn.edu.nju.software.sda.app.service.PairRelationInfoService;
import cn.edu.nju.software.sda.app.service.PairRelationService;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import cn.edu.nju.software.sda.core.domain.node.Node;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PairRelationInfoDao implements InfoDao<PairRelationInfo> {
    @Autowired
    private NodeInfoDao nodeInfoDao;

    @Autowired
    private PairRelationInfoMapper pairRelationInfoMapper;

    @Autowired
    private PairRelationMapper pairRelationMapper;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private PairRelationInfoService pairRelationInfoService;

    @Autowired
    private PairRelationService pairRelationService;

    @Override
    public PairRelationInfo saveProfile(PairRelationInfo info) {
        PairRelationInfoEntity pairRelationInfoEntity = PairRelationInfoEntity.createNewEntity(info);
        pairRelationInfoMapper.insert(pairRelationInfoEntity);
        return info;
    }

    @Override
    public PairRelationInfo saveDetail(PairRelationInfo info) {
        List<PairRelation> pairRelations = new ArrayList<>();
        List<NodeEntity> nodeEntities = nodeService.findByAppid(info.getParentId());
        Map<String, NodeEntity> nodeEntityMap = NodeEntity.toNameNodeMap(nodeEntities);
        for (PairRelation r : info) {
            r.setId(Sid.nextShort());
            NodeEntity ne = nodeEntityMap.get(r.getSourceNode().getName());
            if(ne == null){
                continue;
            }
            r.getSourceNode().setId(ne.getId());
            ne = nodeEntityMap.get(r.getTargetNode().getName());
            if(ne == null){
                continue;
            }
            r.getTargetNode().setId(ne.getId());
            pairRelations.add(r);
        }
        pairRelationMapper.insertList(PairRelationEntity.createNewEntities(info.getId(), pairRelations));

        // 更新
        PairRelationInfoEntity prif = new PairRelationInfoEntity();
        prif.setId(info.getId());
        prif.setStatus(1);
        pairRelationInfoMapper.updateByPrimaryKeySelective(prif);
        return info;
    }

    @Override
    public PairRelationInfo updateProfileInfoById(PairRelationInfo info) {
        PairRelationInfoEntity pairRelationInfoEntity = PairRelationInfoEntity.createNewEntity(info);
        pairRelationInfoMapper.updateByPrimaryKeySelective(pairRelationInfoEntity);
        return info;
    }

    @Override
    public PairRelationInfo deleteById(String infoId) { return null; }

    @Override
    public PairRelationInfo queryProfileInfoById(String infoId) {
        return pairRelationInfoMapper.selectByPrimaryKey(infoId).toPairRelationInfo();
    }

    @Override
    public List<PairRelationInfo> queryProfileInfoByAppId(String appId) {
        List<PairRelationInfoEntity> pairRelationInfoEntities =  pairRelationInfoService.queryPairRelationInfoByAppId(appId);
        List<PairRelationInfo> pairRelationInfos = new ArrayList<>();
        for(PairRelationInfoEntity pairRelationInfoEntity:pairRelationInfoEntities){
            PairRelationInfo pairRelationInfo = pairRelationInfoEntity.toPairRelationInfo();
            pairRelationInfos.add(pairRelationInfo);
        }
        return pairRelationInfos;
    }

    @Override
    public List<PairRelationInfo> queryProfileInfoByAppIdAndInfoName(String appId, String infoName) {
        List<PairRelationInfoEntity> pairRelationInfoEntities =  pairRelationInfoService.queryPairRelationInfoList(appId,infoName);
        List<PairRelationInfo> pairRelationInfos = new ArrayList<>();
        for(PairRelationInfoEntity pairRelationInfoEntity:pairRelationInfoEntities){
            PairRelationInfo pairRelationInfo = pairRelationInfoEntity.toPairRelationInfo();
            pairRelationInfos.add(pairRelationInfo);
        }
        return pairRelationInfos;
    }

    @Override
    public PairRelationInfo queryDetailInfoById(String infoId) {
        PairRelationInfoEntity pairRelationInfoEntity = pairRelationInfoMapper.selectByPrimaryKey(infoId);
        List<PairRelationEntity> pairRelationEntities = pairRelationService.findByInfoId(infoId);
        PairRelationInfo pairRelationInfo = pairRelationInfoEntity.toPairRelationInfo();

        NodeInfo nodeInfo = nodeInfoDao.queryDetailInfoById(pairRelationInfoEntity.getAppId());
        Map<String, Node> idNodeMap = nodeInfo.getNodeSet().getIdNodeMap();

        for(PairRelationEntity pairRelationEntity :pairRelationEntities){
            PairRelation pairRelation = pairRelationEntity.toPairRelation(idNodeMap);
            pairRelationInfo.addRelation(pairRelation);
        }
        return pairRelationInfo;
    }
}
