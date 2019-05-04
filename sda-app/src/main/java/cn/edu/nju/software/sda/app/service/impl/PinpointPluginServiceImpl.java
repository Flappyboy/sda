package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.plugin.Generate;
import cn.edu.nju.software.sda.app.entity.App;
import cn.edu.nju.software.sda.app.entity.ClassNode;
import cn.edu.nju.software.sda.app.service.ClassNodeService;
import cn.edu.nju.software.sda.app.service.PinpointPluginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PinpointPluginServiceImpl implements PinpointPluginService {

    @Autowired
    ClassNodeService classNodeService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void generatePlugin(App app) {
        StringBuilder sb = new StringBuilder();
        List<ClassNode> classNodeList = classNodeService.findByAppid(app.getId());
        for (ClassNode classNode :
                classNodeList) {
            sb.append(classNode.getName()+" ");
        }
        sb.deleteCharAt(sb.length()-1);
        Generate generate = new Generate(app.getName(), sb.toString(),app.getId());
        generate.generateJar();
    }
}
