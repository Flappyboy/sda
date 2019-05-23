package cn.edu.nju.software.sda.plugin;

import cn.edu.nju.software.sda.plugin.utils.PackageUtil;
import org.apache.commons.lang3.ClassUtils;

import java.util.*;

public class PluginManager {

    private static PluginManager instance = new PluginManager();

    private static Map<String, Plugin> namePluginMap = new HashMap<>();

    private PluginManager (){}

    public static PluginManager getInstance(){
        return instance;
    }

    public void register(Plugin plugin){
        plugin.install();
        namePluginMap.put(plugin.getName(), plugin);
    }

    private boolean loaded = false;
    public void load(){
        if(!loaded){
            reload();
            loaded = true;
        }
    }

    public void reload(){
        registerSysPlugin(SysPlugin.class);
        System.out.println("reload plugin end");
    }

    private void registerSysPlugin(Class clazz){
        for(Plugin plugin: PackageUtil.<Plugin>getObjForImplClass(ClassUtils.getPackageName(clazz), Plugin.class)){
            register(plugin);
        }
    }
}
