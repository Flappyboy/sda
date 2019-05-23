package cn.edu.nju.software.sda.plugin;

import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.InfoNameManager;
import cn.edu.nju.software.sda.core.NodeManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.plugin.evaluation.EvaluationAlgorithmManager;
import cn.edu.nju.software.sda.plugin.evaluation.EvaluationPlugin;
import cn.edu.nju.software.sda.plugin.info.InfoCollectionManager;
import cn.edu.nju.software.sda.plugin.info.InfoCollectionPlugin;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithmManager;
import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;
import cn.edu.nju.software.sda.plugin.utils.PackageUtil;
import org.apache.commons.lang3.ClassUtils;

import java.util.Map;

public class SysPlugin implements Plugin {

    private static Map<Class<? extends Info>, InfoDao> infoDaoMap;

    public static void setInfoDaoMap(Map<Class<? extends Info>, InfoDao> infoDaoMap){
        SysPlugin.infoDaoMap = infoDaoMap;
    }

    @Override
    public String getName() {
        return "SYS_PLUGIN";
    }

    @Override
    public String getDesc() {
        return "SYS";
    }

    @Override
    public void install() {

        for(EvaluationPlugin plugin: PackageUtil.<EvaluationPlugin>getObjForImplClass(ClassUtils.getPackageName(EvaluationPlugin.class), EvaluationPlugin.class)){
            EvaluationAlgorithmManager.register(plugin);
        }
        for(PartitionPlugin plugin: PackageUtil.<PartitionPlugin>getObjForImplClass(ClassUtils.getPackageName(PartitionPlugin.class), PartitionPlugin.class)){
            PartitionAlgorithmManager.register(plugin);
        }
        for(InfoCollectionPlugin plugin: PackageUtil.<InfoCollectionPlugin>getObjForImplClass(ClassUtils.getPackageName(InfoCollectionPlugin.class), InfoCollectionPlugin.class)){
            InfoCollectionManager.register(plugin);
        }

        for (Map.Entry<Class<? extends Info>, InfoDao> entry : infoDaoMap.entrySet()) {
            InfoDaoManager.register(entry.getKey(), entry.getValue());
        }
//        InfoDaoManager.register();

        InfoNameManager.register(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT, PairRelationInfo.class);
        InfoNameManager.register(PairRelation.INFO_NAME_STATIC_METHOD_CALL_COUNT, PairRelationInfo.class);
        InfoNameManager.register(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT, PairRelationInfo.class);
        InfoNameManager.register(PairRelation.INFO_NAME_DYNAMIC_METHOD_CALL_COUNT, PairRelationInfo.class);
        InfoNameManager.register(GroupRelation.GIT_COMMIT, GroupRelationInfo.class);
        InfoNameManager.register(Node.INFO_NAME_NODE, NodeInfo.class);
        InfoNameManager.register(Partition.INFO_NAME_PARTITION, PartitionInfo.class);

        NodeManager.register("SYS_CLASS_NODE", ClassNode.class);
        NodeManager.register("SYS_METHOD_NODE", MethodNode.class);
    }

    @Override
    public void uninstall() {

    }
}
