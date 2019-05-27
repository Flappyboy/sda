package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.PairRelationInfoMapper;
import cn.edu.nju.software.sda.app.entity.PairRelationInfoEntity;
import cn.edu.nju.software.sda.app.service.AppService;
import cn.edu.nju.software.sda.app.service.PairRelationInfoService;
import cn.edu.nju.software.sda.app.service.PairRelationService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PairRelationInfoServiceImpl implements PairRelationInfoService {

    @Autowired
    private PairRelationInfoMapper pairRelationInfoMapper;
    @Autowired
    private PairRelationService pairRelationService;
    @Autowired
    private AppService appService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PairRelationInfoEntity save(PairRelationInfoEntity dAnalysisInfo) {
        String id = Sid.nextShort();
        dAnalysisInfo.setId(id);
        dAnalysisInfo.setCreatedAt(new Date());
        dAnalysisInfo.setUpdatedAt(new Date());
        dAnalysisInfo.setFlag(1);
        dAnalysisInfo.setStatus(0);
        pairRelationInfoMapper.insertSelective(dAnalysisInfo);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug("statistics start");
                pairRelationService.statisticsCallInfo(dAnalysisInfo);
                log.debug("statistics end");
            }
        });
        thread.start();

        return dAnalysisInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDAnalysisInfo(PairRelationInfoEntity dAnalysisInfo) {
        dAnalysisInfo.setUpdatedAt(new Date());
        pairRelationInfoMapper.updateByPrimaryKeySelective(dAnalysisInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDAnalysisInfo(String dAnalysisInfoId) {
        PairRelationInfoEntity dAnalysisInfo = new PairRelationInfoEntity();
        dAnalysisInfo.setId(dAnalysisInfoId);
        dAnalysisInfo.setFlag(0);
        updateDAnalysisInfo(dAnalysisInfo);
    }

    @Override
    public PairRelationInfoEntity queryDAnalysisInfoById(String dAnalysisInfoId) {
        PairRelationInfoEntity obj = pairRelationInfoMapper.selectByPrimaryKey(dAnalysisInfoId);
        return obj;
    }

    @Override
    public List<PairRelationInfoEntity> queryDAnalysisInfoListPaged(Integer page, Integer pageSize, String appId, String desc) {
        PageHelper.startPage(page, pageSize);

        Example example = new Example(PairRelationInfoEntity.class);

        PairRelationInfoEntity demo = new PairRelationInfoEntity();
        demo.setFlag(1);

        if(StringUtils.isNotBlank(appId)){
            demo.setAppId(appId);
        }
        if(StringUtils.isNotBlank(desc)){
            demo.setDesc(desc);
        }
        example.createCriteria().andEqualTo(demo);
        example.setOrderByClause("created_at desc");
        List<PairRelationInfoEntity> pairRelationInfoEntityList = pairRelationInfoMapper.selectByExample(example);
        for (PairRelationInfoEntity pairRelationInfoEntity :
                pairRelationInfoEntityList) {
            pairRelationInfoEntity.setAppName(appService.queryAppById(pairRelationInfoEntity.getAppId()).getName());
        }
        return pairRelationInfoEntityList;
    }

    @Override
    public List<PairRelationInfoEntity> queryPairRelationInfoList(String appId, String name) {
        Example example = new Example(PairRelationInfoEntity.class);

        PairRelationInfoEntity demo = new PairRelationInfoEntity();
        demo.setFlag(1);

        if(StringUtils.isNotBlank(appId)){
            demo.setAppId(appId);
        }
        if(StringUtils.isNotBlank(name)){
            demo.setName(name);
        }
        example.createCriteria().andEqualTo(demo);
        example.setOrderByClause("created_at desc");
        List<PairRelationInfoEntity> pairRelationInfoEntityList = pairRelationInfoMapper.selectByExample(example);
        for (PairRelationInfoEntity pairRelationInfoEntity :
                pairRelationInfoEntityList) {
            pairRelationInfoEntity.setAppName(appService.queryAppById(pairRelationInfoEntity.getAppId()).getName());
        }
        return pairRelationInfoEntityList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfDAnalysisInfo(String appId,String desc) {
        Example example = new Example(PairRelationInfoEntity.class);

        PairRelationInfoEntity demo = new PairRelationInfoEntity();
        demo.setFlag(1);

        if(StringUtils.isNotBlank(appId)){
            demo.setAppId(appId);
        }
        if(StringUtils.isNotBlank(desc)){
            demo.setDesc(desc);
        }
        example.createCriteria().andEqualTo(demo);
        return pairRelationInfoMapper.selectCountByExample(example);
    }
}
