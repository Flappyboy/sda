package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class InfoController {

    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JSONResult allInfos(String appId) {
        InfoSet infoSet = infoService.queryInfoByAppId(appId);
        return JSONResult.ok(infoSet);
    }
}
