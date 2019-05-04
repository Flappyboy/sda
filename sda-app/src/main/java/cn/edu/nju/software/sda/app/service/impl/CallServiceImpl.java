package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.DynamicCallInfoMapper;
import cn.edu.nju.software.sda.app.dao.StaticCallInfoMapper;
import cn.edu.nju.software.sda.app.entity.CallInfo;
import cn.edu.nju.software.sda.app.entity.DynamicCallInfo;
import cn.edu.nju.software.sda.app.entity.StaticCallInfo;
import cn.edu.nju.software.sda.app.service.CallService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class CallServiceImpl implements CallService {
    @Autowired
    DynamicCallInfoMapper dynamicCallInfoMapper;

    @Autowired
    StaticCallInfoMapper staticCallInfoMapper;

    @Override
    public List<CallInfo> findCallInfo(String nodeId, String dynamicAnalysisInfoId) {
        List<DynamicCallInfo> dynamicCallInfoList = new ArrayList<>();
        Example example = null;

        if(StringUtils.isNotBlank(dynamicAnalysisInfoId)) {
            DynamicCallInfo dynamicCallInfo = new DynamicCallInfo();
            dynamicCallInfo.setCaller(nodeId);
            dynamicCallInfo.setDynamicAnalysisInfoId(dynamicAnalysisInfoId);
            dynamicCallInfo.setFlag(1);
            example = new Example(DynamicCallInfo.class);
            example.createCriteria().andEqualTo(dynamicCallInfo);
            dynamicCallInfoList.addAll(dynamicCallInfoMapper.selectByExample(example));

            dynamicCallInfo = new DynamicCallInfo();
            dynamicCallInfo.setCallee(nodeId);
            dynamicCallInfo.setDynamicAnalysisInfoId(dynamicAnalysisInfoId);
            dynamicCallInfo.setFlag(1);
            example = new Example(DynamicCallInfo.class);
            example.createCriteria().andEqualTo(dynamicCallInfo);
            dynamicCallInfoList.addAll(dynamicCallInfoMapper.selectByExample(example));
        }

        StaticCallInfo staticCallInfo = new StaticCallInfo();
        staticCallInfo.setCaller(nodeId);
        staticCallInfo.setFlag(1);
        example = new Example(StaticCallInfo.class);
        example.createCriteria().andEqualTo(staticCallInfo);
        List<StaticCallInfo> staticCallInfoList = staticCallInfoMapper.selectByExample(example);

        staticCallInfo = new StaticCallInfo();
        staticCallInfo.setCallee(nodeId);
        staticCallInfo.setFlag(1);
        example = new Example(StaticCallInfo.class);
        example.createCriteria().andEqualTo(staticCallInfo);
        staticCallInfoList.addAll(staticCallInfoMapper.selectByExample(example));

        return CallInfo.creatCallInfoList(dynamicCallInfoList, staticCallInfoList);
    }
}
