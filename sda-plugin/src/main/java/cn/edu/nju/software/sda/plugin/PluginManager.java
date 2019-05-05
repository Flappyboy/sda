package cn.edu.nju.software.sda.plugin;

import cn.edu.nju.software.sda.plugin.base.SdaPlugin;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithm;
import cn.edu.nju.software.sda.plugin.utils.PackageUtil;
import org.apache.commons.lang3.ClassUtils;

import java.util.*;

public class PluginManager {

    private static PluginManager instance = new PluginManager();

    private static Map<String, PartitionAlgorithm> partitionAlgorithmMap= new HashMap<>();

    private PluginManager (){}

    public static PluginManager getInstance(){
        instance.load();
        return instance;
    }

    public void register(SdaPlugin sdaPlugin){
        if(sdaPlugin instanceof PartitionAlgorithm){
            PartitionAlgorithm pa = (PartitionAlgorithm) sdaPlugin;
            partitionAlgorithmMap.put(pa.getName(), pa);
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
        for(SdaPlugin sdaPlugin: PackageUtil.<SdaPlugin>getObjForImplClass(ClassUtils.getPackageName(PartitionAlgorithm.class), SdaPlugin.class)){
            register(sdaPlugin);
        }
        System.out.println("reload");
    }

    public List<PartitionAlgorithm> getPartitionAlgorithmList() {
        return new ArrayList<>(partitionAlgorithmMap.values());
    }

    public <P extends SdaPlugin> P getPlugin(Class<P> clazz, String name){
        if(PartitionAlgorithm.class.isAssignableFrom(clazz)){
            return (P) partitionAlgorithmMap.get(name);
        }
        return null;
    }
}
