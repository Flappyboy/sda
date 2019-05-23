package cn.edu.nju.software.sda.core.domain.info;

import java.util.HashMap;
import java.util.Map;

public class InfoSetMap {

    Map<String, InfoSet> infos = new HashMap<>();

    public void addInfo(Info info){
        InfoSet infoSet = infos.get(info.getName());
        if(infoSet == null){
            infoSet = new InfoSet();
            infos.put(info.getName(), infoSet);
        }
        infoSet.addInfo(info);
    }
}
