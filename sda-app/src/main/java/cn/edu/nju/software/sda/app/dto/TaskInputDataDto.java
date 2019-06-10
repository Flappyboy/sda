package cn.edu.nju.software.sda.app.dto;

import cn.edu.nju.software.sda.core.domain.dto.InputData;
import cn.edu.nju.software.sda.core.domain.info.Info;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TaskInputDataDto {
    private Map<String, List<InfoDto>> infoDatas = new HashMap<>();

    private Map<String, List<String>> formDatas = new HashMap<>();

    public InputData toInputData(){
        InputData inputData = new InputData();

        Map<String, List<Info>> infoDatasMap = new HashMap<>();
        for (Map.Entry<String, List<InfoDto>> entry :
                this.infoDatas.entrySet()) {
            List<Info> list = new ArrayList<>();
            for (InfoDto infoDto :
                    entry.getValue()) {
                list.add(infoDto);
            }
            infoDatasMap.put(entry.getKey(), list);
        }
        inputData.setInfoDatas(infoDatasMap);
        inputData.setFormDatas(this.formDatas);
        return inputData;
    }
}
