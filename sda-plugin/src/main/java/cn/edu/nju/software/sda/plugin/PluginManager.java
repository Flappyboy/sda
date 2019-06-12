package cn.edu.nju.software.sda.plugin;

import cn.edu.nju.software.sda.core.utils.WorkspaceUtil;
import cn.edu.nju.software.sda.plugin.utils.PackageUtil;
import org.apache.commons.lang3.ClassUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
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
//        registerSysPlugin(SysPlugin.class);
        register(new SysPlugin());
        ClassLoader serviceCL = null;
        try {
            File dir = new File(WorkspaceUtil.absolutePath("/plugins"));
            if(!dir.exists()){
                dir.mkdirs();
            }else{
                if(!dir.isDirectory()){
                    dir.delete();
                    dir.mkdirs();
                }
            }
            List<URL> urls = new ArrayList<>();
            for (File jar:
                    dir.listFiles()) {
                if(jar.getName().endsWith(".jar")){
                    urls.add(jar.toURI().toURL());
                }
            }
            serviceCL = new URLClassLoader(
                    urls.toArray(new URL[urls.size()]),
                    Plugin.class.getClassLoader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class, serviceCL);
        for (Plugin plugin : loader) {
            register(plugin);
        }
        System.out.println("reload plugin end");
    }

    private void registerSysPlugin(Class clazz){
        for(Plugin plugin: PackageUtil.<Plugin>getObjForImplClass(ClassUtils.getPackageName(clazz), Plugin.class)){
            register(plugin);
        }
    }
}
