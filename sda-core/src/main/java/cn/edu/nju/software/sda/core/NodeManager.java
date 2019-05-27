package cn.edu.nju.software.sda.core;

import cn.edu.nju.software.sda.core.domain.node.Node;

import java.util.HashMap;
import java.util.Map;

public class NodeManager {
    public static Map<String, Class> nodeClassMap = new HashMap<>();
    public static Map<Class, String> classNodeMap = new HashMap<>();

    private final static NodeManager nodeManager = new NodeManager();

    private NodeManager() {
    }

    public static void register(Class clazz){
        register(clazz.getName(), clazz);
    }

    public static void register(String name, Class clazz){
        if(clazz == null){
            throw new RuntimeException("clazz is null");
        }
        if(!Node.class.isAssignableFrom(clazz)){
            throw new RuntimeException("clazz "+clazz.getName()+" is not subclass of Node");
        }
        nodeClassMap.put(name, clazz);
        classNodeMap.put(clazz, name);
    }

    public static Class getClass(String node){
        return nodeClassMap.get(node);
    }

    public static String getNode(Class clazz){
        return classNodeMap.get(clazz);
    }
}
