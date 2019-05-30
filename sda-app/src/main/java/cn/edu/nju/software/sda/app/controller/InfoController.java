package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.NodeInfoDto;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.app.service.NodeService;
import cn.edu.nju.software.sda.core.InfoDaoManager;
import cn.edu.nju.software.sda.core.InfoNameManager;
import cn.edu.nju.software.sda.core.dao.InfoDao;
import cn.edu.nju.software.sda.core.dao.InfoManager;
import cn.edu.nju.software.sda.core.domain.evaluation.Evaluation;
import cn.edu.nju.software.sda.core.domain.info.InfoSet;
import cn.edu.nju.software.sda.core.domain.info.NodeInfo;
import cn.edu.nju.software.sda.core.domain.node.Node;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/info")
public class InfoController {

    @Autowired
    InfoService infoService;

    @Autowired
    NodeService nodeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity queryInfoByAppIdAndName(@RequestParam String appId, @RequestParam String name) {
        InfoSet infoSet = infoService.queryInfoByAppIdAndName(appId, name);
        return ResponseEntity.ok(infoSet.getInfoList());
    }

    @RequestMapping(value = "/node", method = RequestMethod.GET)
    public ResponseEntity queryNodeInfo(@RequestParam String appId) {
        NodeInfoDto nodeInfoDto = nodeService.queryNodeInfoByAppId(appId);
        return ResponseEntity.ok(nodeInfoDto);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity deleteApps(@RequestParam String name,@RequestBody List<String> ids) {
        InfoDao infoDao = InfoDaoManager.getInfoDao(name);
        if(infoDao == null){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("The corresponding InfoDao for "+name+" is missing!");
        }
        for (String id :
                ids) {
            InfoDaoManager.getInfoDao(name).deleteById(id);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public ResponseEntity infoType() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Class> infoClassNameMap = InfoNameManager.getInfoClassNameMap();
        Map<Class, List<String>> map = new HashMap<>();
        for (Map.Entry<String, Class> entry :
                infoClassNameMap.entrySet()) {
            List<String> names = map.get(entry.getValue());
            if(names == null){
                names = new ArrayList<>();
                map.put(entry.getValue(), names);
            }
            names.add(entry.getKey());
        }
        for (Map.Entry<Class, List<String>> entry :
                map.entrySet()) {
            if(entry.getKey().equals(NodeInfo.class) || entry.getKey().equals(Evaluation.class)){
                continue;
            }
            Map<String, Object> item = new HashMap();
            item.put("label", entry.getKey().getSimpleName());
            item.put("children", entry.getValue());
            list.add(item);
        }
        list.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String name1 = (String) o1.get("label");
                String name2 = (String) o1.get("label");
                return name1.compareToIgnoreCase(name2);
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("nameDescMap", InfoNameManager.getInfoDescMap());
        result.put("types", list);
        return ResponseEntity.ok(result);
    }
}
