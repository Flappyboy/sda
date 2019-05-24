package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.InfoCollectionDto;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/task")
public class TaskController {

    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/info_collection/functions", method = RequestMethod.GET)
    public JSONResult findInfoCollections() {
        List<FunctionService> pluginList = PluginFunctionManager.get(FunctionType.InfoCollection);
        return JSONResult.ok(pluginList);
    }
    @RequestMapping(value = "/partition/functions", method = RequestMethod.GET)
    public JSONResult findPartitions() {
        List<FunctionService> pluginList = PluginFunctionManager.get(FunctionType.Partition);
        return JSONResult.ok(pluginList);
    }
    @RequestMapping(value = "/evaluation/functions", method = RequestMethod.GET)
    public JSONResult findEvaluations() {
        List<FunctionService> pluginList = PluginFunctionManager.get(FunctionType.Evaluation);
        return JSONResult.ok(pluginList);
    }

    @RequestMapping(value = "/do", method = RequestMethod.POST)
    public JSONResult doTask(@RequestBody InfoCollectionDto dto) {
        ResultDto resultDto = PluginFunctionManager.check(dto.getPluginName(), dto.getInputData());
        if(!resultDto.getOk()){
            return JSONResult.errorMsg(resultDto.getMsg());
        }
        infoService.collectInfo(dto.getAppId(), PluginFunctionManager.get(dto.getPluginName()), dto.getInputData());
        return JSONResult.ok();
    }
}
