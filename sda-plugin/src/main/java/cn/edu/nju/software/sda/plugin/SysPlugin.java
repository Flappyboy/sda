package cn.edu.nju.software.sda.plugin;

import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.InfoNameManager;
import cn.edu.nju.software.sda.core.NodeManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.info.*;
import cn.edu.nju.software.sda.core.domain.node.ClassNode;
import cn.edu.nju.software.sda.core.domain.node.MethodNode;
import cn.edu.nju.software.sda.core.domain.node.Node;
import cn.edu.nju.software.sda.core.domain.partition.Partition;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import cn.edu.nju.software.sda.plugin.function.evaluation.EvaluationAlgorithm;
import cn.edu.nju.software.sda.plugin.function.evaluation.impl.demo.DemoEvaluationAlgorithm;
import cn.edu.nju.software.sda.plugin.function.evaluation.impl.metric.MetricEvaluationAlgorithm;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;
import cn.edu.nju.software.sda.plugin.function.info.impl.demo.Demo;
import cn.edu.nju.software.sda.plugin.function.info.impl.pinpointdynamicjava.PinpointDynamicJavaInfoCollection;
import cn.edu.nju.software.sda.plugin.function.info.impl.pinpointplugin.PinpointPluginFileInfoCollection;
import cn.edu.nju.software.sda.plugin.function.info.impl.staticjava.StaticJavaInfoCollection;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;
import cn.edu.nju.software.sda.plugin.function.partition.impl.demo.DemoPartitionAlgorithm;
import cn.edu.nju.software.sda.plugin.function.partition.impl.louvain.LouvainPartitionAlgorithm;
import cn.edu.nju.software.sda.plugin.function.partition.impl.mst.MSTPartitionAlgorithm;
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

        /*for(EvaluationAlgorithm plugin: PackageUtil.<EvaluationAlgorithm>getObjForImplClass(ClassUtils.getPackageName(EvaluationAlgorithm.class), EvaluationAlgorithm.class)){
            PluginFunctionManager.register(plugin);
        }
        for(PartitionAlgorithm plugin: PackageUtil.<PartitionAlgorithm>getObjForImplClass(ClassUtils.getPackageName(PartitionAlgorithm.class), PartitionAlgorithm.class)){
            PluginFunctionManager.register(plugin);
        }
        for(InfoCollection plugin: PackageUtil.<InfoCollection>getObjForImplClass(ClassUtils.getPackageName(InfoCollection.class), InfoCollection.class)){
            PluginFunctionManager.register(plugin);
        }*/

        PluginFunctionManager.register(new PinpointPluginFileInfoCollection());
        PluginFunctionManager.register(new Demo());
        PluginFunctionManager.register(new StaticJavaInfoCollection());
        PluginFunctionManager.register(new PinpointDynamicJavaInfoCollection());
        PluginFunctionManager.register(new LouvainPartitionAlgorithm());
        PluginFunctionManager.register(new DemoEvaluationAlgorithm());
        PluginFunctionManager.register(new DemoPartitionAlgorithm());
        PluginFunctionManager.register(new MetricEvaluationAlgorithm());
        PluginFunctionManager.register(new MSTPartitionAlgorithm());

        for (Map.Entry<Class<? extends Info>, InfoDao> entry : infoDaoMap.entrySet()) {
            InfoDaoManager.register(entry.getKey(), entry.getValue());
        }
//        InfoDaoManager.register();

        InfoNameManager.register(PairRelation.INFO_NAME_STATIC_CLASS_CALL_COUNT, PairRelationInfo.class, "静态收集的类调用关系及数量");
        InfoNameManager.register(PairRelation.INFO_NAME_STATIC_METHOD_CALL_COUNT, PairRelationInfo.class, "静态收集的方法调用关系及数量");
        InfoNameManager.register(PairRelation.INFO_NAME_DYNAMIC_CLASS_CALL_COUNT, PairRelationInfo.class, "动态收集的类调用关系及数量");
        InfoNameManager.register(PairRelation.INFO_NAME_DYNAMIC_METHOD_CALL_COUNT, PairRelationInfo.class, "动态收集的方法调用关系及数量");
        InfoNameManager.register(GroupRelation.GIT_COMMIT, GroupRelationInfo.class, "从git的commit记录中收集的类之间的关系");
        InfoNameManager.register(Node.INFO_NAME_NODE, NodeInfo.class, "节点信息");
        InfoNameManager.register(Partition.INFO_NAME_PARTITION, PartitionInfo.class, "划分结果信息");
        InfoNameManager.register(PinpointPluginFileInfoCollection.INFO_NAME, FileInfo.class, "生成的pinpoint的插件");

        NodeManager.register("SYS_CLASS_NODE", ClassNode.class);
        NodeManager.register("SYS_METHOD_NODE", MethodNode.class);
    }

    @Override
    public void uninstall() {

    }
}
