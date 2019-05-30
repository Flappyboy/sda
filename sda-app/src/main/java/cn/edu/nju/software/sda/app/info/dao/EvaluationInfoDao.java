package cn.edu.nju.software.sda.app.info.dao;

import cn.edu.nju.software.sda.app.dao.*;
import cn.edu.nju.software.sda.app.entity.*;
import cn.edu.nju.software.sda.app.service.*;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import cn.edu.nju.software.sda.core.domain.evaluation.Indicator;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.core.domain.partition.PartitionNode;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Component
public class EvaluationInfoDao implements InfoDao<EvaluationInfo> {

    @Autowired
    EvaluationInfoMapper evaluationInfoMapper;

    @Autowired
    EvaluationIndicatorMapper evaluationIndicatorMapper;
    
    @Autowired
    private TaskService taskService;

    @Override
    public  EvaluationInfo saveProfile( EvaluationInfo info) {
        evaluationInfoMapper.insert( EvaluationInfoEntity.create(info));
        return info;
    }

    @Override
    public  EvaluationInfo saveDetail( EvaluationInfo info) {
        Evaluation evaluation = info.getEvaluation();

        List<EvaluationIndicatorEntity> entities = new ArrayList<>();
        for(Indicator indicator : evaluation.getIndicators()){
            EvaluationIndicatorEntity entity = new EvaluationIndicatorEntity();
            entity.setId(Sid.nextShort());
            entity.setInfoId(info.getId());
            entity.setName(indicator.getName());
            entity.setValue(indicator.getValue());
            entities.add(entity);
        }
        if(entities.size()>0)
            evaluationIndicatorMapper.insertList(entities);
        return info;
    }

    @Override
    public  EvaluationInfo updateProfileInfoById( EvaluationInfo info) {
        return null;
    }

    @Override
    public  EvaluationInfo deleteById(String infoId) {
        return null;
    }

    @Override
    public  EvaluationInfo queryProfileInfoById(String infoId) {
        return null;
    }

    @Override
    public List< EvaluationInfo> queryProfileInfoByAppId(String appId) {
        return null;
    }

    @Override
    public List< EvaluationInfo> queryProfileInfoByAppIdAndInfoName(String appId, String infoName) {
        return null;
    }

    @Override
    public  EvaluationInfo queryDetailInfoById(String infoId) {
        return null;
    }
}
