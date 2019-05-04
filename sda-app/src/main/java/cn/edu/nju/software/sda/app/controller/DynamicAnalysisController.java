package cn.edu.nju.software.sda.app.controller;

import cn.edu.nju.software.sda.app.entity.DynamicAnalysisInfo;
import cn.edu.nju.software.sda.app.entity.common.JSONResult;
import cn.edu.nju.software.sda.app.service.DynamicAnalysisInfoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "动态分析信息接口")
@RequestMapping(value = "/api")
public class DynamicAnalysisController {
    @Autowired
    private DynamicAnalysisInfoService dynamicAnalysisInfoService;

    @ApiModelProperty(value = "dynamicAnalysisInfo", notes = "动态分析信息的json串")
    @ApiOperation(value = "新增动态分析信息", notes = "返回状态200成功")
    @RequestMapping(value = "/dynaInfo", method = RequestMethod.POST)
    public JSONResult addAnalysis(@RequestBody DynamicAnalysisInfo dynamicAnalysisInfo) throws Exception {
        DynamicAnalysisInfo dynamicAnalysisInfo1 = dynamicAnalysisInfoService.saveDAnalysisInfo(dynamicAnalysisInfo);
        return JSONResult.ok(dynamicAnalysisInfo1.getId());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "page", value = "分页：页码",  dataType = "int"),
            @ApiImplicitParam(paramType="query", name = "pageSize", value = "分页：每页大小（默认大小100）",  dataType = "int")
    })
    @ApiOperation(value = "分页查询动态分析信息", notes = "返回状态200成功")
    @RequestMapping(value = "/dynaInfo", method = RequestMethod.GET)
    public JSONResult queryAnalysisListPaged(Integer page, Integer pageSize, String appid, String appName, String desc) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }
        List<DynamicAnalysisInfo> mylist = dynamicAnalysisInfoService.queryDAnalysisInfoListPaged(page, pageSize,appid, appName,desc);
        int count = dynamicAnalysisInfoService.countOfDAnalysisInfo(appid, appName, desc);
        HashMap<String ,Object> result = new HashMap<String ,Object>();
        result.put("list",mylist);
        result.put("total",count);
        return JSONResult.ok(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "id", value = "动态分析Id", required = true, dataType = "String"),
    })
    @ApiOperation(value = "删除动态分析", notes = "返回状态200成功")
    @RequestMapping(value = "/dynaInfo/{id}", method = RequestMethod.DELETE)
    public JSONResult deleteDAInfo(@PathVariable  String id) throws Exception {
        dynamicAnalysisInfoService.deleteDAnalysisInfo(id);
        return JSONResult.ok();
    }
}
