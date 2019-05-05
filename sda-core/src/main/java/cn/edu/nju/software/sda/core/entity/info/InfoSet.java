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
    
    public Map<String, RelationInfo> getRelationInfo(Class relationClass, Class nodeClass){
        Map<String, RelationInfo> relationInfoMap = new HashMap<>();
        for (Info info :
                infoList) {
            if (info.getClass().equals(RelationInfo.class)){
                RelationInfo relationInfo = (RelationInfo) info;
                if(relationInfo.getRelationClass().equals(relationClass) && relationInfo.getNodeClass().equals(relationClass)){
                    relationInfoMap.put(relationInfo.getName(), relationInfo);
                }
            }
        }
        return relationInfoMap;
    }
}
