package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.NodeMapper;
import cn.edu.nju.software.sda.app.entity.NodeEntity;
import cn.edu.nju.software.sda.app.service.NodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {
    @Autowired
    private NodeMapper nodeMapper;

    @Override
    public NodeEntity findById(String id) {
        return nodeMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<NodeEntity> findBycondition(String name, String appid) {
        Example example = new Example(NodeEntity.class);

        NodeEntity demo = new NodeEntity();
        if(StringUtils.isNoneBlank(name)){
            demo.setName(name);
        }
        if(StringUtils.isNoneBlank(appid)){
            demo.setAppId(appid);
        }
        example.createCriteria().andEqualTo(demo);
        List<NodeEntity> calleeNodes = nodeMapper.selectByExample(example);
        return calleeNodes;
    }

    @Override
    public List<NodeEntity> findByAppid(String appid) {
        Example example = new Example(NodeEntity.class);

        NodeEntity demo = new NodeEntity();

        if(StringUtils.isNoneBlank(appid)){
            demo.setAppId(appid);
        }
        example.createCriteria().andEqualTo(demo);
        List<NodeEntity> calleeNodes = nodeMapper.selectByExample(example);
        return calleeNodes;
    }
}
