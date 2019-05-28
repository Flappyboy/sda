package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.InfoCollectionDto;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.app.service.TaskService;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @RequestMapping(value = "/do", method = RequestMethod.POST)
    public ResponseEntity doTask(@RequestBody TaskEntity taskEntity) {
        taskEntity.setTaskDataList(null);
        ResultDto resultDto = PluginFunctionManager.check(taskEntity.getFunctionName(), taskEntity.getInputDataDto().toInputData());
        if(!resultDto.getOk()){
            return ResponseEntity.ok(resultDto);
        }
        taskEntity = taskService.newTask(taskEntity);
        return ResponseEntity.ok(taskEntity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity queryTaskById(@PathVariable String id) {
        TaskEntity taskEntity = taskService.queryTaskEntityById(id);
        return ResponseEntity.ok(taskEntity);
    }
}
