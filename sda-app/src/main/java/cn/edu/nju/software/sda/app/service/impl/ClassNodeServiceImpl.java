package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.ClassNodeMapper;
import cn.edu.nju.software.sda.app.entity.ClassNode;
import cn.edu.nju.software.sda.app.entity.ClassNodeExample;
import cn.edu.nju.software.sda.app.service.ClassNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassNodeServiceImpl implements ClassNodeService {
    @Autowired
    private ClassNodeMapper classNodeMapper;

    @Override
    public ClassNode findById(String id) {
        ClassNode classnode = new ClassNode();
        ClassNodeExample classNodeExample = new ClassNodeExample();
        ClassNodeExample.Criteria classNodeCriteria = classNodeExample.createCriteria();
        classNodeCriteria.andIdEqualTo(id).andFlagEqualTo(1);
        List<ClassNode> calleeNodes = classNodeMapper.selectByExample(classNodeExample);
        if (calleeNodes.size() > 0 && calleeNodes != null)
            classnode = calleeNodes.get(0);
        return classnode;
    }

    @Override
    public List<ClassNode> findBycondition(String name, String appid) {
        ClassNodeExample classNodeExample = new ClassNodeExample();
        ClassNodeExample.Criteria classNodeCriteria = classNodeExample.createCriteria();
        classNodeCriteria.andNameEqualTo(name).andAppidEqualTo(appid).andFlagEqualTo(1);
        List<ClassNode> calleeNodes = classNodeMapper.selectByExample(classNodeExample);
        return calleeNodes;
    }

    @Override
    public List<ClassNode> findByAppid(String appid) {
        ClassNodeExample classNodeExample = new ClassNodeExample();
        ClassNodeExample.Criteria classNodeCriteria = classNodeExample.createCriteria();
        classNodeCriteria.andAppidEqualTo(appid).andFlagEqualTo(1);
        List<ClassNode> calleeNodes = classNodeMapper.selectByExample(classNodeExample);
        return calleeNodes;
    }
}
