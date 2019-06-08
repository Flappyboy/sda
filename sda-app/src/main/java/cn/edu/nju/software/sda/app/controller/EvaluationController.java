package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.EvaluationInfoEntity;
import cn.edu.nju.software.sda.app.entity.PartitionInfoEntity;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.app.service.AppService;
import cn.edu.nju.software.sda.app.service.EvaluationInfoService;
import cn.edu.nju.software.sda.app.service.PartitionService;
import cn.edu.nju.software.sda.app.service.TaskService;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/evaluation")
public class EvaluationController {

    @Autowired
    EvaluationInfoService evaluationInfoService;

    @Autowired
    PartitionService partitionService;

    @RequestMapping(value = "/last", method = RequestMethod.GET)
    public ResponseEntity queryLastEvaluation(@RequestParam String partitionId) {
        EvaluationInfoEntity evaluationInfoEntity = evaluationInfoService.queryLastEvaluationByPartitionId(partitionId);
        return ResponseEntity.ok(evaluationInfoEntity);
    }

    @RequestMapping(value = "/last", method = RequestMethod.POST)
    public ResponseEntity queryLastEvaluation(@RequestBody List<String> partitionIds) {
        List<PartitionInfoEntity> entities = new ArrayList<>();
        for (String partitionId :
                partitionIds) {
            PartitionInfoEntity partitionInfoEntity = partitionService.findPartitionById(partitionId);
            EvaluationInfoEntity evaluationInfoEntity = evaluationInfoService.queryLastEvaluationByPartitionId(partitionId);
            partitionInfoEntity.setLastEvaluation(evaluationInfoEntity);
            entities.add(partitionInfoEntity);
        }
        return ResponseEntity.ok(entities);
    }

    @RequestMapping(value = "/redo", method = RequestMethod.GET)
    public ResponseEntity redo(@RequestParam String id) {
        String taskId = evaluationInfoService.redo(id);
        return ResponseEntity.ok(taskId);
    }
}
