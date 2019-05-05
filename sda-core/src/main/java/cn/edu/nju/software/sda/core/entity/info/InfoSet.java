package cn.edu.nju.software.sda.core.entity.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoSet {
    private List<Info> infoList = new ArrayList<>();
    
    public void addInfo(Info info){
        infoList.add(info);
    }
    
    public <R extends Relation> Map<String, RelationInfo<R>> getRelationInfo(Class<R> relationClass, Class nodeClass){
        Map<String, RelationInfo<R>> relationInfoMap = new HashMap<>();
        for (Info info :
                infoList) {
            if (info.getClass().isAssignableFrom(RelationInfo.class)){
                RelationInfo relationInfo = (RelationInfo) info;
                if(relationClass.isAssignableFrom(relationInfo.getRelationClass()) && nodeClass.isAssignableFrom(relationInfo.getNodeClass())){
                    relationInfoMap.put(relationInfo.getName(), relationInfo);
                }
            }
        }
        return relationInfoMap;
    }

    public <R extends Relation> RelationInfo<R> getRelationInfo(Class<R> relationClass, Class nodeClass, String name){
        return getRelationInfo(relationClass,nodeClass).get(name);
    }
}
