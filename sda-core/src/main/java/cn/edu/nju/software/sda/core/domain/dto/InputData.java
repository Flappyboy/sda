package cn.edu.nju.software.sda.core.domain.dto;

import cn.edu.nju.software.sda.core.dao.InfoManager;
import cn.edu.nju.software.sda.core.domain.info.Info;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@NoArgsConstructor
public class InputData {

    Map<String, List<Info>> infoDatas;

    Map<String, List<String>> formDatas;

    Map<String, List<Info>> infoDataObjs;

    Map<String, List<Object>> formDataObjs;

    public Map<String, List<Info>> getInfoDataObjs(){
        if(infoDataObjs != null){
            return infoDataObjs;
        }
        infoDataObjs = new HashMap<>();
        for (Map.Entry<String, List<Info>> entry: infoDatas.entrySet()){
            List<Info> infoList = new ArrayList<>();
            for (Info i :
                    entry.getValue()) {
                Info info = InfoManager.queryInfoByNameAndId(i.getName(), i.getId());
                infoList.add(info);
            }
            infoDataObjs.put(entry.getKey(), infoList);
        }
        return infoDataObjs;
    }

    public InfoSet getInfoSet(){
        InfoSet infoSet = new InfoSet(getInfoDataObjs());
        return infoSet;
    }

    public Map<String, List<Object>> getFormDataObjs(MetaData metaData){
        if(formDataObjs != null){
            return formDataObjs;
        }
        formDataObjs = new HashMap<>();
        for (Map.Entry<String, List<String>> entry: formDatas.entrySet()){
            List<Object> list = new ArrayList<>();
            for (String value :
                    entry.getValue()) {
                Object object = metaData.getFormDataTypeByName(entry.getKey()).getObj(value);
                list.add(object);
            }
            formDataObjs.put(entry.getKey(), list);
        }
        return formDataObjs;
    }

    public Map<String, List<Info>> getInfoDatas() {
        return infoDatas;
    }

    public Map<String, List<String>> getFormDatas() {
        return formDatas;
    }
}
