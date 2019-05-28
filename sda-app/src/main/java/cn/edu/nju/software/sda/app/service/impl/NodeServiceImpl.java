package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.dao.NodeMapper;
import cn.edu.nju.software.sda.app.dto.NodeInfoDto;
import cn.edu.nju.software.sda.app.entity.NodeEntity;
import cn.edu.nju.software.sda.app.service.NodeService;
import cn.edu.nju.software.sda.core.domain.node.Node;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, NodeEntity> findNameMapByAppid(String appid) {
        List<NodeEntity> nodeEntities = findByAppid(appid);
        Map<String, NodeEntity> nodeEntityMap = new HashMap<>();
        for (NodeEntity node :
                nodeEntities) {
            nodeEntityMap.put(node.getName(), node);
        }
        return nodeEntityMap;
    }

    @Override
    public void deleteByAppid(String appid) {
        Example example = new Example(NodeEntity.class);

        NodeEntity demo = new NodeEntity();

        if(StringUtils.isNoneBlank(appid)){
            demo.setAppId(appid);
        }
        example.createCriteria().andEqualTo(demo);
        nodeMapper.deleteByExample(example);
    }

    @Override
    public NodeInfoDto queryNodeInfoByAppId(String appId) {
        NodeInfoDto nodeInfoDto = new NodeInfoDto();

        NodeEntity nodeEntity = new NodeEntity();
        nodeEntity.setAppId(appId);
        nodeInfoDto.setNodeCount(nodeMapper.selectCount(nodeEntity));

        return nodeInfoDto;
    }
}
