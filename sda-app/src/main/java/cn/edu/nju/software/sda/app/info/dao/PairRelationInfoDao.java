package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.dao.PairRelationInfoMapper;
import cn.edu.nju.software.sda.app.dao.PairRelationMapper;
import cn.edu.nju.software.sda.app.entity.NodeEntity;
import cn.edu.nju.software.sda.app.entity.PairRelationEntity;
import cn.edu.nju.software.sda.app.entity.PairRelationInfoEntity;
import cn.edu.nju.software.sda.app.service.NodeService;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.PairRelation;
import cn.edu.nju.software.sda.core.domain.info.PairRelationInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PairRelationInfoDao implements InfoDao<PairRelationInfo> {

    @Autowired
    private PairRelationInfoMapper pairRelationInfoMapper;

    @Autowired
    private PairRelationMapper pairRelationMapper;

    @Autowired
    private NodeService nodeService;

    @Override
    public PairRelationInfo save(PairRelationInfo info) {
        PairRelationInfoEntity pairRelationInfoEntity = PairRelationInfoEntity.createNewEntity(info);
        pairRelationInfoMapper.insert(pairRelationInfoEntity);
        List<PairRelation> pairRelations = new ArrayList<>();
        List<NodeEntity> nodeEntities = nodeService.findByAppid(info.getAppId());
        Map<String, NodeEntity> nodeEntityMap = NodeEntity.toNameNodeMap(nodeEntities);
        for (PairRelation r :
                info) {
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

        PairRelationInfoEntity prif = new PairRelationInfoEntity();
        prif.setId(pairRelationInfoEntity.getId());
        prif.setStatus(1);
        pairRelationInfoMapper.updateByPrimaryKeySelective(prif);
        return info;
    }

    @Override
    public PairRelationInfo updateById(PairRelationInfo info) {
        return null;
    }

    @Override
    public PairRelationInfo deleteById(PairRelationInfo info) {
        return null;
    }

    @Override
    public List<PairRelationInfo> querySimpleInfoByAppId(String appId) {
        return null;
    }

    @Override
    public List<PairRelationInfo> querySimpleInfoByAppId(String appId, Integer pageSize, Integer page) {
        return null;
    }

    @Override
    public Integer countOfQuerySimpleInfoByAppId(String appId) {
        return null;
    }

    @Override
    public PairRelationInfo queryDetailInfoById(String infoId) {
        return null;
    }
}