package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.InfoCollectionDto;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/function")
public class FunctionController {

    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public ResponseEntity findFunctionsByType(@PathVariable FunctionType type) {
        List<FunctionService> pluginList = PluginFunctionManager.get(type);
        return ResponseEntity.ok(pluginList);
    }
}
