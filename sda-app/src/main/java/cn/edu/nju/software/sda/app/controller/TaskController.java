package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.dto.InfoCollectionDto;
import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.entity.TaskEntity;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.AppService;
import cn.edu.nju.software.sda.app.service.InfoService;
import cn.edu.nju.software.sda.app.service.TaskService;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import cn.edu.nju.software.sda.core.domain.dto.ResultDto;
import cn.edu.nju.software.sda.core.service.FunctionType;
import cn.edu.nju.software.sda.core.service.FunctionService;
import cn.edu.nju.software.sda.plugin.function.PluginFunctionManager;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    AppService appService;

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

    @RequestMapping(value = "/terminate/{id}", method = RequestMethod.GET)
    public ResponseEntity terminateTaskById(@PathVariable String id) {
        TaskEntity taskEntity = taskService.queryTaskEntityById(id);
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        for (int i = 0; i < noThreads; i++) {
            String nm = lstThreads[i].getName();
            if (nm.equals(taskEntity.getId())) {
                lstThreads[i].stop();
                return ResponseEntity.ok(taskEntity);
            }
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("终止失败，或任务已结束！");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity queryTasks(String appName, String type, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setAppName(StringUtils.stripToNull(appName));
        taskEntity.setType(StringUtils.stripToNull(type));
        PageQueryDto<TaskEntity> dto = taskService.queryTaskPaged(taskEntity, PageQueryDto.create(pageNum, pageSize));
        return ResponseEntity.ok(dto);
    }
}
