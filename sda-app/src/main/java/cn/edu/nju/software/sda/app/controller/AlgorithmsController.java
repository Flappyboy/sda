package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.PluginDto;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
@Api(value = "算法相关接口")
@RequestMapping(value = "/api/algorithm")
public class AlgorithmsController {

    @RequestMapping(value = "/partition", method = RequestMethod.GET)
    public JSONResult getPartitionAlgorithms() {
        List<PluginDto> pluginDtoList = new ArrayList<>();
        for (FunctionService pa :
                PluginFunctionManager.get(FunctionType.Partition)) {
            PluginDto pluginDto = new PluginDto();
            pluginDto.setId(pa.getName());
            pluginDto.setDesc(pa.getDesc());
            pluginDtoList.add(pluginDto);
        }
        return JSONResult.ok(pluginDtoList);
    }

}
