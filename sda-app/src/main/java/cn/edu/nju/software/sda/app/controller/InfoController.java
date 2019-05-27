package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/info")
public class InfoController {

    @Autowired
    InfoService infoService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity queryInfoByAppIdAndName(@RequestParam String appId, @RequestParam String name) {
        InfoSet infoSet = infoService.queryInfoByAppIdAndName(appId, name);
        return ResponseEntity.ok(infoSet.getInfoList());
    }
}
