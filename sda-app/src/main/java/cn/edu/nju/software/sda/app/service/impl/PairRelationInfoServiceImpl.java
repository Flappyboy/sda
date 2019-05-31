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
    public void updateInfo(PairRelationInfoEntity dAnalysisInfo) {
        dAnalysisInfo.setUpdatedAt(new Date());
        pairRelationInfoMapper.updateByPrimaryKeySelective(dAnalysisInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteInfo(String dAnalysisInfoId) {
        PairRelationInfoEntity dAnalysisInfo = new PairRelationInfoEntity();
        dAnalysisInfo.setId(dAnalysisInfoId);
        dAnalysisInfo.setFlag(0);
        updateInfo(dAnalysisInfo);
    }

    @Override
    public PairRelationInfoEntity queryInfoById(String infoId) {
        PairRelationInfoEntity obj = pairRelationInfoMapper.selectByPrimaryKey(infoId);
        return obj;
    }

    @Override
    public List<PairRelationInfoEntity> queryInfoListPaged(Integer page, Integer pageSize, String appId, String desc) {
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
    public List<PairRelationInfoEntity> queryPairRelationInfoByAppId(String appId) {
        Example example = new Example(PairRelationInfoEntity.class);

        PairRelationInfoEntity demo = new PairRelationInfoEntity();
        demo.setFlag(1);

        if(StringUtils.isNotBlank(appId)){
            demo.setAppId(appId);
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
    public int countOfInfo(String appId,String desc) {
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
