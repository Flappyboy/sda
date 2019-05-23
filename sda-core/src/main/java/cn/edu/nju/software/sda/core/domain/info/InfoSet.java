package cn.edu.nju.software.sda.core.domain.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoSet {
    private Map<String, List<Info>> infoListMap = new HashMap<>();
    
    public void addInfo(Info info){
        List<Info> list = infoListMap.get(info.getName());
        if(list == null){
            list = new ArrayList<>();
            infoListMap.put(info.getName(), list);
        }
        list.add(info);
    }

    public Info getRelationInfo(String name){
        List<Info> list = infoListMap.get(name);
        if(list == null || list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }
}
