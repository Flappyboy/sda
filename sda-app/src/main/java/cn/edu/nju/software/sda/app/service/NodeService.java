package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.dto.NodeInfoDto;
import cn.edu.nju.software.sda.app.entity.NodeEntity;

import java.util.List;
import java.util.Map;

public interface NodeService {

    NodeEntity findById(String id);

    List<NodeEntity> findBycondition(String name, String appid);

    List<NodeEntity> findByAppid(String appid);

    Map<String, NodeEntity> findNameMapByAppid(String appid);

    void deleteByAppid(String appid);

    NodeInfoDto queryNodeInfoByAppId(String appId);
}
