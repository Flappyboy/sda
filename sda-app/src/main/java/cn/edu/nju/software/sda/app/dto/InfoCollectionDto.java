package cn.edu.nju.software.sda.app.dto;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.meta.MetaData;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class InfoCollectionDto {

    String appId;

    String pluginName;

    String desc;

    MetaData metaData;

    InputData inputData;

    public static List<InfoCollectionDto> wrap(List<InfoCollection> pluginList){
        List<InfoCollectionDto> objList = new ArrayList<>();
        for (InfoCollection plugin :
                pluginList) {
            objList.add(wrap(plugin));
        }
        return objList;
    }

    public static InfoCollectionDto wrap(InfoCollection plugin){
        InfoCollectionDto obj = new InfoCollectionDto();
        obj.setPluginName(plugin.getName());
        obj.setDesc(plugin.getDesc());
        obj.setMetaData(plugin.getMetaData());
        return obj;
    }
}
