package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.AppMapper;
import cn.edu.nju.software.sda.app.entity.App;
import cn.edu.nju.software.sda.app.entity.AppExample;
import cn.edu.nju.software.sda.app.service.AppService;
import cn.edu.nju.software.sda.app.service.StaticCallService;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;

    @Autowired
    private StaticCallService staticCallService;

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
    @Transactional(propagation = Propagation.SUPPORTS)
    public App queryAppById(String appId) {
        App app = null;
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(appId).andFlagEqualTo(1);
        List<App> apps = appMapper.selectByExample(example);
        if (apps.size() > 0 && apps != null)
            app = apps.get(0);
        return app;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
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
}
