package cn.edu.nju.software.sda.app.service;

import cn.edu.nju.software.sda.app.entity.ClassNode;

import java.util.List;

public interface ClassNodeService {

    public ClassNode findById(String id);

    public List<ClassNode> findBycondition(String name, String appid);

    List<ClassNode> findByAppid(String appid);
}
