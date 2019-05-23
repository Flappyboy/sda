package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.PluginDto;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.AlgorithmsService;
import cn.edu.nju.software.sda.plugin.function.partition.PartitionAlgorithm;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
@Api(value = "算法相关接口")
@RequestMapping(value = "/api/algorithm")
public class AlgorithmsController {
    @Autowired
    private AlgorithmsService algorithmsService;

    @RequestMapping(value = "/partition", method = RequestMethod.GET)
    public JSONResult getPartitionAlgorithms() {
        List<PluginDto> pluginDtoList = new ArrayList<>();
        for (PartitionAlgorithm pa :
                algorithmsService.getAllAvailablePartitionAlgorithm()) {
            PluginDto pluginDto = new PluginDto();
            pluginDto.setId(pa.getName());
            pluginDto.setDesc(pa.getDesc());
            pluginDtoList.add(pluginDto);
        }
        return JSONResult.ok(pluginDtoList);
    }

}
