package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.NodeEntity;

import java.util.List;

public interface NodeService {

    public NodeEntity findById(String id);

    public List<NodeEntity> findBycondition(String name, String appid);

    List<NodeEntity> findByAppid(String appid);

    void deleteByAppid(String appid);
}
