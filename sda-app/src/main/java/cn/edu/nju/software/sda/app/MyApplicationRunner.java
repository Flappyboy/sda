package cn.edu.nju.software.sda.app;

import cn.edu.nju.software.sda.app.info.dao.*;
import cn.edu.nju.software.sda.core.config.SdaConfig;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.evaluation.EvaluationInfo;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.PluginManager;
import cn.edu.nju.software.sda.plugin.SysPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component//被spring容器管理
@Order(100)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class MyApplicationRunner implements ApplicationRunner {
    @Value("${sda.properties}")
    private String propertiesName;

    @Autowired
    private PairRelationInfoDao pairRelationInfoDao;

    @Autowired
    private NodeInfoDao nodeInfoDao;

    @Autowired
    private PartitionInfoDao partitionInfoDao;

    @Autowired
    private EvaluationInfoDao evaluationInfoDao;

    @Autowired
    private GroupRelationInfoDao groupRelationInfoDao;

    @Autowired
    private FileInfoDao fileInfoDao;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        SdaConfig.loadAllProperties(propertiesName);
        WorkspaceUtil.init();

        Map<Class<? extends Info>, InfoDao> infoDaoMap = new HashMap<>();
        infoDaoMap.put(PairRelationInfo.class, pairRelationInfoDao);
        infoDaoMap.put(NodeInfo.class, nodeInfoDao);
        infoDaoMap.put(PartitionInfo.class, partitionInfoDao);
        infoDaoMap.put(EvaluationInfo.class, evaluationInfoDao);
        infoDaoMap.put(GroupRelationInfo.class, groupRelationInfoDao);
        infoDaoMap.put(FileInfo.class, fileInfoDao);
        SysPlugin.setInfoDaoMap(infoDaoMap);
        PluginManager.getInstance().reload();
    }
}
