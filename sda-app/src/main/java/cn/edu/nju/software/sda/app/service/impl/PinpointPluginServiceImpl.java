package cn.edu.nju.software.sda.app.service.impl;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.entity.NodeEntity;
import cn.edu.nju.software.sda.app.plugin.Generate;
import cn.edu.nju.software.sda.app.service.NodeService;
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
    NodeService nodeService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void generatePlugin(AppEntity app) {
        StringBuilder sb = new StringBuilder();
        List<NodeEntity> nodeEntityList = nodeService.findByAppid(app.getId());
        for (NodeEntity nodeEntity :
                nodeEntityList) {
            sb.append(nodeEntity.getName()+" ");
        }
        sb.deleteCharAt(sb.length()-1);
        Generate generate = new Generate(app.getName(), sb.toString(),app.getId());
        generate.generateJar();
    }
}
