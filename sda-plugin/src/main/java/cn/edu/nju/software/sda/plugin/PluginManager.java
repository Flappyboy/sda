package cn.edu.nju.software.sda.plugin;

import cn.edu.nju.software.sda.plugin.base.SdaPlugin;
import cn.edu.nju.software.sda.plugin.evaluation.EvaluationPlugin;
import cn.edu.nju.software.sda.plugin.partition.PartitionPlugin;
import cn.edu.nju.software.sda.plugin.utils.PackageUtil;
import org.apache.commons.lang3.ClassUtils;

import java.util.*;

public class PluginManager {

    private static PluginManager instance = new PluginManager();

    private static Map<String, PartitionPlugin> partitioPluginMap = new HashMap<>();

    private static Map<String, EvaluationPlugin> evaluationPluginMap= new HashMap<>();

    private PluginManager (){}

    public static PluginManager getInstance(){
        instance.load();
        return instance;
    }

    public void register(SdaPlugin sdaPlugin){
        if(sdaPlugin instanceof PartitionPlugin){
            PartitionPlugin pa = (PartitionPlugin) sdaPlugin;
            partitioPluginMap.put(pa.getName(), pa);
        }
        if(sdaPlugin instanceof EvaluationPlugin){
            EvaluationPlugin ep = (EvaluationPlugin) sdaPlugin;
            evaluationPluginMap.put(ep.getName(), ep);
        }
    }

    private boolean loaded = false;
    public void load(){
        if(!loaded){
            reload();
            loaded = true;
        }
    }

    public void reload(){
        for(SdaPlugin sdaPlugin: PackageUtil.<SdaPlugin>getObjForImplClass(ClassUtils.getPackageName(PartitionPlugin.class), SdaPlugin.class)){
            register(sdaPlugin);
        }
        for(SdaPlugin sdaPlugin: PackageUtil.<SdaPlugin>getObjForImplClass(ClassUtils.getPackageName(EvaluationPlugin.class), SdaPlugin.class)){
            register(sdaPlugin);
        }
        System.out.println("reload");
    }

    public List<PartitionPlugin> getPartitionPluginList() {
        return new ArrayList<>(partitioPluginMap.values());
    }
    public List<EvaluationPlugin> getEvaluationPluginList() {
        return new ArrayList<>(evaluationPluginMap.values());
    }

    public <P extends SdaPlugin> P getPlugin(Class<P> clazz, String name){
        if(PartitionPlugin.class.isAssignableFrom(clazz)){
            return (P) partitioPluginMap.get(name);
        }
        if(EvaluationPlugin.class.isAssignableFrom(clazz)){
            return (P) evaluationPluginMap.get(name);
        }
        return null;
    }
}
