package cn.edu.nju.software.sda.plugin;

import cn.edu.nju.software.sda.plugin.base.BaseService;
import cn.edu.nju.software.sda.plugin.partition.PartitionAlgorithm;
import cn.edu.nju.software.sda.plugin.utils.PackageUtil;
import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginManager {

    private static PluginManager instance = new PluginManager();

    private static Map<String, PartitionAlgorithm> partitionAlgorithmMap= new HashMap<>();

    private PluginManager (){}

    public static PluginManager getInstance(){
        instance.load();
        return instance;
    }

    public void register(BaseService baseService){
        if(baseService instanceof PartitionAlgorithm){
            PartitionAlgorithm pa = (PartitionAlgorithm) baseService;
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
        for(BaseService baseServie: PackageUtil.<BaseService>getObjForImplClass(ClassUtils.getPackageName(PartitionAlgorithm.class), BaseService.class)){
            register(baseServie);
        }
        System.out.println("reload");
    }

    public List<PartitionAlgorithm> getPartitionAlgorithmList() {
        return new ArrayList<>(partitionAlgorithmMap.values());
    }
}
