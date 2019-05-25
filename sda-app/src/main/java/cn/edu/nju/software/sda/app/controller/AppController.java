package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.service.AppService;
import cn.edu.nju.software.sda.core.domain.PageQueryDto;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "应用的相关接口")
@RequestMapping(value = "/api")
public class AppController {
    @Autowired
    private AppService appService;


    @ApiModelProperty(value = "app", notes = "项目信息的json串")
    @ApiOperation(value = "新增项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app", method = RequestMethod.POST)
    public ResponseEntity addApp(@RequestBody AppEntity app) {
        appService.saveApp(app);
        return ResponseEntity.ok(app);
    }

    @ApiModelProperty(value = "app", notes = "项目信息的json串")
    @ApiOperation(value = "更新项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app", method = RequestMethod.PUT)
    public ResponseEntity updateApp(@RequestBody AppEntity app) {
        AppEntity appEntity = appService.updateApp(app);
        return ResponseEntity.ok(appEntity);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "项目appId", required = true, dataType = "String"),
    })
    @ApiOperation(value = "删除项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteApp(@PathVariable String id) {
        appService.deleteApp(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "项目appId", required = true, dataType = "String"),
    })
    @ApiOperation(value = "删除项目", notes = "返回状态200成功")
    @RequestMapping(value = "/apps", method = RequestMethod.DELETE)
    public ResponseEntity deleteApps(@RequestBody List<String> ids) {
        for (String id :
                ids) {
            appService.deleteApp(id);
        }
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "项目appId", required = true, dataType = "String"),
    })
    @ApiOperation(value = "根据id查询项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app/{id}", method = RequestMethod.GET)
    public ResponseEntity queryAppById(@PathVariable String id) {
        AppEntity app = appService.queryAppById(id);
        return ResponseEntity.ok(app);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "pageNum", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小10）",  dataType = "int")
    })
    @ApiOperation(value = "分页查询项目列表", notes = "返回状态200成功")
    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public ResponseEntity queryAppListPaged(Integer pageNum, Integer pageSize, String name, String desc) {
        name = StringUtils.stripToNull(name);
        desc = StringUtils.stripToNull(desc);
        AppEntity appEntity = AppEntity.createByNameAndDesc(name, desc);
        PageQueryDto<AppEntity> pageQueryDto = appService.queryAppListByLikePaged(PageQueryDto.create(pageNum, pageSize), appEntity);
        return ResponseEntity.ok(pageQueryDto);
    }

}
