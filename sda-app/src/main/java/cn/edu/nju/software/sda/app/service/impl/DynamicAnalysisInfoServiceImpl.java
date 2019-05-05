package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.AppMapper;
import cn.edu.nju.software.sda.app.dao.DynamicAnalysisInfoMapper;
import cn.edu.nju.software.sda.app.entity.App;
import cn.edu.nju.software.sda.app.entity.AppExample;
import cn.edu.nju.software.sda.app.entity.DynamicAnalysisInfo;
import cn.edu.nju.software.sda.app.entity.DynamicAnalysisInfoExample;
import cn.edu.nju.software.sda.app.service.AppService;
import cn.edu.nju.software.sda.app.service.DynamicAnalysisInfoService;
import cn.edu.nju.software.sda.app.service.DynamicCallService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DynamicAnalysisInfoServiceImpl implements DynamicAnalysisInfoService {

    @Autowired
    private DynamicAnalysisInfoMapper dynamicAnalysisInfoMapper;
    @Autowired
    private DynamicCallService dynamicCallService;
    @Autowired
    private AppService appService;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public DynamicAnalysisInfo saveDAnalysisInfo(DynamicAnalysisInfo dAnalysisInfo) {
        String id = sid.nextShort();
        dAnalysisInfo.setId(id);
        dAnalysisInfo.setCreatedat(new Date());
        dAnalysisInfo.setUpdatedat(new Date());
        dAnalysisInfo.setFlag(1);
        dAnalysisInfo.setStatus(0);
        dynamicAnalysisInfoMapper.insertSelective(dAnalysisInfo);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug("statistics start");
                dynamicCallService.statisticsCallInfo(dAnalysisInfo);
                log.debug("statistics end");
            }
        });
        thread.start();

        return dAnalysisInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDAnalysisInfo(DynamicAnalysisInfo dAnalysisInfo) {
        dAnalysisInfo.setUpdatedat(new Date());
        DynamicAnalysisInfoExample example = new DynamicAnalysisInfoExample();
        DynamicAnalysisInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(dAnalysisInfo.getId()).andFlagEqualTo(1);
        dynamicAnalysisInfoMapper.updateByExampleSelective(dAnalysisInfo, example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDAnalysisInfo(String dAnalysisInfoId) {
        DynamicAnalysisInfo dAnalysisInfo = new DynamicAnalysisInfo();
        dAnalysisInfo.setId(dAnalysisInfoId);
        dAnalysisInfo.setFlag(0);
        dAnalysisInfo.setUpdatedat(new Date());
        DynamicAnalysisInfoExample example = new DynamicAnalysisInfoExample();
        DynamicAnalysisInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(dAnalysisInfoId).andFlagEqualTo(1);
        dynamicAnalysisInfoMapper.updateByExampleSelective(dAnalysisInfo, example);
    }

    @Override
    public DynamicAnalysisInfo queryDAnalysisInfoById(String dAnalysisInfoId) {
        DynamicAnalysisInfo dAnalysisInfo = new DynamicAnalysisInfo();
        DynamicAnalysisInfoExample example = new DynamicAnalysisInfoExample();
        DynamicAnalysisInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(dAnalysisInfoId).andFlagEqualTo(1);
        List<DynamicAnalysisInfo> apps = dynamicAnalysisInfoMapper.selectByExample(example);
        if (apps.size() > 0 && apps != null)
            dAnalysisInfo = apps.get(0);
        return dAnalysisInfo;
    }

    @Override
    public List<DynamicAnalysisInfo> queryDAnalysisInfoListPaged(Integer page, Integer pageSize,String appId, String appName, String desc) {
        PageHelper.startPage(page, pageSize);

        DynamicAnalysisInfoExample example = new DynamicAnalysisInfoExample();
        DynamicAnalysisInfoExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);
        if(!StringUtils.isEmpty(appId)){
            criteria.andAppidEqualTo(appId);
        }
        if(appName!=null&&appName!=""&&!appName.isEmpty()){
            AppExample appexample = new AppExample();
            AppExample.Criteria appcriteria = appexample.createCriteria();
            appcriteria.andFlagEqualTo(1).andIdEqualTo(appId);
            List<App> apps = appMapper.selectByExample(appexample);
            List<String> appIds = new ArrayList<>();
            for(App app :apps){
                appIds.add(app.getId());
            }
            criteria.andAppidIn(appIds);
        }
        if(desc!=null&&desc!=""&&!desc.isEmpty()){
            criteria.andDescEqualTo(desc);
        }
        example.setOrderByClause("createdAt desc");
        List<DynamicAnalysisInfo> dynamicAnalysisInfoList = dynamicAnalysisInfoMapper.selectByExample(example);
        for (DynamicAnalysisInfo dynamicAnalysisInfo :
                dynamicAnalysisInfoList) {
            dynamicAnalysisInfo.setAppName(appService.queryAppById(dynamicAnalysisInfo.getAppid()).getName());
        }
        return dynamicAnalysisInfoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countOfDAnalysisInfo(String appId, String appName,String desc) {
        DynamicAnalysisInfoExample example = new DynamicAnalysisInfoExample();
        DynamicAnalysisInfoExample.Criteria criteria = example.createCriteria();
        criteria.andFlagEqualTo(1);
        if(!StringUtils.isEmpty(appId)){
            criteria.andAppidEqualTo(appId);
        }
        if(appName!=null&&appName!=""&&!appName.isEmpty()){
            AppExample appexample = new AppExample();
            AppExample.Criteria appcriteria = appexample.createCriteria();
            appcriteria.andFlagEqualTo(1).andNameEqualTo(appName);
            List<App> apps = appMapper.selectByExample(appexample);
            List<String> appIds = new ArrayList<>();
            for(App app :apps){
                appIds.add(app.getId());
            }
            criteria.andAppidIn(appIds);
        }
        if(desc!=null&&desc!=""&&!desc.isEmpty()){
            criteria.andDescEqualTo(desc);
        }
        return dynamicAnalysisInfoMapper.countByExample(example);
    }
}
