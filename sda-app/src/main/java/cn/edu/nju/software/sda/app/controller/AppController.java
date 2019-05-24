package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.AppEntity;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.AppService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "项目App相关接口")
@RequestMapping(value = "/api")
public class AppController {
    @Autowired
    private AppService appService;


    @ApiModelProperty(value = "app", notes = "项目信息的json串")
    @ApiOperation(value = "新增项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app", method = RequestMethod.POST)
    public JSONResult addApp(@RequestBody AppEntity app) throws Exception {
        appService.saveApp(app);
        return JSONResult.ok(app);
    }

    @ApiModelProperty(value = "app", notes = "项目信息的json串")
    @ApiOperation(value = "更新项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app", method = RequestMethod.PUT)
    public JSONResult updateApp(@RequestBody AppEntity app) throws Exception {
        appService.updateApp(app);
        return JSONResult.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "项目appId", required = true, dataType = "String"),
    })
    @ApiOperation(value = "删除项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app/{id}", method = RequestMethod.DELETE)
    public JSONResult deleteApp(@PathVariable String id) throws Exception {
        appService.deleteApp(id);
        return JSONResult.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "项目appId", required = true, dataType = "String"),
    })
    @ApiOperation(value = "根据id查询项目", notes = "返回状态200成功")
    @RequestMapping(value = "/app/{id}", method = RequestMethod.GET)
    public JSONResult queryAppById(@PathVariable String id) throws Exception {
        AppEntity app = appService.queryAppById(id);
        return JSONResult.ok(app);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小100）",  dataType = "int")
    })
    @ApiOperation(value = "分页查询项目列表", notes = "返回状态200成功")
    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public JSONResult queryAppListPaged(Integer page, Integer pageSize,String appName, String desc) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }
        AppEntity appEntity = new AppEntity();
        appEntity.setName(appName);
        appEntity.setDesc(desc);
        List<AppEntity> appList = appService.queryUserListPaged(page, pageSize,appEntity);
        int count = appService.countOfApp(appEntity);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",appList);
        result.put("total",count);
        return JSONResult.ok(result);
    }

}
