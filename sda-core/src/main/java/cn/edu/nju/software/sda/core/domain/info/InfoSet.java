package cn.edu.nju.software.sda.core.domain.info;

import cn.edu.nju.software.sda.core.InfoNameManager;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class InfoSet {
    private Map<String, List<Info>> infoListMap = new HashMap<>();

    public InfoSet(Info info) {
        addInfo(info);
    }

    public InfoSet(Collection<Info> infoList) {
        if(infoList == null){
            return;
        }
        for (Info info :
                infoList) {
            addInfo(info);
        }
    }

    public InfoSet(Map<String, List<Info>> infoListMap) {
        this.infoListMap = infoListMap;
    }

    public void addInfo(Info info){
        List<Info> list = infoListMap.get(info.getName());
        if(list == null){
            list = new ArrayList<>();
            infoListMap.put(info.getName(), list);
        }
        list.add(info);
    }

    public List<Info> getInfoListByName(String name){
        return infoListMap.get(name);
    }

    public List<Info> getInfoList(){
        List<Info> infoList = new ArrayList<>();
        for (Map.Entry<String, List<Info>> entry:
            infoListMap.entrySet()){
            infoList.addAll(entry.getValue());
        }
        return infoList;
    }

    public Info getInfoByName(String name) {
        List<Info> list = infoListMap.get(name);
        if(list == null || list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }

    public <I extends Info> List<I> getInfoListByClass(Class<I> clazz) {
        List<String> names = InfoNameManager.getNamesByClass(clazz);
        List<I> list = new ArrayList<>();
        for (String name:
                names) {
            list.addAll((Collection<? extends I>) getInfoListByName(name));
        }
        return list;
    }
    public <I extends Info> I getInfoByClass(Class<I> clazz) {
        List<String> names = InfoNameManager.getNamesByClass(clazz);
        for (String name:
                names) {
            I info = (I) getInfoByName(name);
            if(info != null){
                return info;
            }
        }
        return null;
    }

}
