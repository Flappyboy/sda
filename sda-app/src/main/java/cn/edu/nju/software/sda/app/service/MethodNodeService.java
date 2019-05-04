package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.MethodNode;

import java.util.List;

public interface MethodNodeService {
    public MethodNode findById(String id);
    public List<MethodNode> findByCondition(String name,String classname,String appid);
}
