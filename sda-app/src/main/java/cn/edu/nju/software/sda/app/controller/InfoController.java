package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.InfoCollectionDto;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.plugin.function.FunctionType;
import cn.edu.nju.software.sda.plugin.function.PluginFunction;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import cn.edu.nju.software.sda.plugin.function.info.InfoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class InfoController {

    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/info/collection/plugins", method = RequestMethod.GET)
    public JSONResult findCollectionPlugins() {
        List<PluginFunction> pluginList = PluginFunctionManager.get(FunctionType.InfoCollection);
        return JSONResult.ok(pluginList);
    }

    @RequestMapping(value = "/info/collection/collect", method = RequestMethod.POST)
    public JSONResult collectInfo(@RequestBody InfoCollectionDto dto) {
        infoService.collectInfo(dto.getAppId(), PluginFunctionManager.get(dto.getPluginName()), dto.getInputData());
        return JSONResult.ok();
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JSONResult allInfos(String appId) {
        InfoSet infoSet = infoService.queryInfoByAppId(appId);
        return JSONResult.ok(infoSet);
    }
}
