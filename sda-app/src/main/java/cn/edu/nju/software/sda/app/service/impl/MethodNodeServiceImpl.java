package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.MethodNodeMapper;
import cn.edu.nju.software.sda.app.entity.MethodNode;
import cn.edu.nju.software.sda.app.entity.MethodNodeExample;
import cn.edu.nju.software.sda.app.service.MethodNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MethodNodeServiceImpl implements MethodNodeService {
    @Autowired
    private MethodNodeMapper methodNodeMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public MethodNode findById(String id) {
        MethodNode methodNode = new MethodNode();
        MethodNodeExample example = new MethodNodeExample();
        MethodNodeExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id).andFlagEqualTo(1);
        List<MethodNode> methodNodes = methodNodeMapper.selectByExample(example);
        if (methodNodes.size() > 0 && methodNodes != null)
            methodNode = methodNodes.get(0);
        return methodNode;
    }

    @Override
    public List<MethodNode> findByCondition(String name, String classname, String appid) {
        MethodNodeExample example = new MethodNodeExample();
        MethodNodeExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name).andAppidEqualTo(appid).andFlagEqualTo(1);
        if(classname!=null)
            criteria.andClassnameEqualTo(classname);
        List<MethodNode> methodNodes = methodNodeMapper.selectByExample(example);
        return methodNodes;
    }
}
